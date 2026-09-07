package com.kiblatsaya.app.domain.qibla

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class QiblaLocation(
    val name: String,
    val country: String,
    val coordinates: Coordinates
)

data class QiblaInfo(
    val qiblaBearing: Float,
    val distanceKm: Double,
    val cardinalDirection: String
)

object QiblaCalculator {
    const val KAABA_LATITUDE = 21.422487
    const val KAABA_LONGITUDE = 39.826206
    private const val EARTH_RADIUS_KM = 6371.0

    val DEFAULT_LOCATIONS = listOf(
        // Malaysia
        QiblaLocation("Kuala Lumpur", "Malaysia", Coordinates(3.1390, 101.6869)),
        QiblaLocation("Putrajaya", "Malaysia", Coordinates(2.9264, 101.6964)),
        QiblaLocation("Shah Alam", "Selangor, Malaysia", Coordinates(3.0738, 101.5183)),
        QiblaLocation("George Town", "Pulau Pinang, Malaysia", Coordinates(5.4164, 100.3327)),
        QiblaLocation("Johor Bahru", "Johor, Malaysia", Coordinates(1.4927, 103.7414)),
        QiblaLocation("Kota Kinabalu", "Sabah, Malaysia", Coordinates(5.9804, 116.0735)),
        QiblaLocation("Kuching", "Sarawak, Malaysia", Coordinates(1.5533, 110.3592)),
        QiblaLocation("Ipoh", "Perak, Malaysia", Coordinates(4.5975, 101.0901)),
        QiblaLocation("Melaka", "Malaysia", Coordinates(2.1896, 102.2501)),
        QiblaLocation("Kota Bharu", "Kelantan, Malaysia", Coordinates(6.1254, 102.2386)),
        QiblaLocation("Kuantan", "Pahang, Malaysia", Coordinates(3.8126, 103.3256)),
        QiblaLocation("Alor Setar", "Kedah, Malaysia", Coordinates(6.1248, 100.3678)),
        QiblaLocation("Kuala Terengganu", "Terengganu, Malaysia", Coordinates(5.3117, 103.1324)),
        QiblaLocation("Seremban", "Negeri Sembilan, Malaysia", Coordinates(2.7258, 101.9424)),
        QiblaLocation("Kangar", "Perlis, Malaysia", Coordinates(6.4414, 100.1986)),

        // Indonesia
        QiblaLocation("Jakarta", "Indonesia", Coordinates(-6.2088, 106.8456)),
        QiblaLocation("Surabaya", "Jawa Timur, Indonesia", Coordinates(-7.2575, 112.7521)),
        QiblaLocation("Bandung", "Jawa Barat, Indonesia", Coordinates(-6.9175, 107.6191)),
        QiblaLocation("Medan", "Sumatera Utara, Indonesia", Coordinates(3.5952, 98.6722)),
        QiblaLocation("Semarang", "Jawa Tengah, Indonesia", Coordinates(-6.9667, 110.4167)),
        QiblaLocation("Makassar", "Sulawesi Selatan, Indonesia", Coordinates(-5.1477, 119.4327)),
        QiblaLocation("Palembang", "Sumatera Selatan, Indonesia", Coordinates(-2.9761, 104.7754)),
        QiblaLocation("Yogyakarta", "D.I. Yogyakarta, Indonesia", Coordinates(-7.7956, 110.3695)),
        QiblaLocation("Banda Aceh", "Aceh, Indonesia", Coordinates(5.5483, 95.3238)),
        QiblaLocation("Denpasar", "Bali, Indonesia", Coordinates(-8.6705, 115.2126)),
        QiblaLocation("Balikpapan", "Kalimantan Timur, Indonesia", Coordinates(-1.2379, 116.8289)),
        QiblaLocation("Pontianak", "Kalimantan Barat, Indonesia", Coordinates(-0.0263, 109.3425)),
        QiblaLocation("Padang", "Sumatera Barat, Indonesia", Coordinates(-0.9471, 100.4172)),
        QiblaLocation("Banjarmasin", "Kalimantan Selatan, Indonesia", Coordinates(-3.3194, 114.5908)),

        // Brunei Darussalam
        QiblaLocation("Bandar Seri Begawan", "Brunei Darussalam", Coordinates(4.9031, 114.9398)),
        QiblaLocation("Kuala Belait", "Brunei Darussalam", Coordinates(4.5833, 114.2333)),
        QiblaLocation("Tutong", "Brunei Darussalam", Coordinates(4.8000, 114.6500)),
        QiblaLocation("Bangar", "Temburong, Brunei Darussalam", Coordinates(4.7167, 115.0667)),

        // Thailand (Selatan & Pusat)
        QiblaLocation("Pattani", "Thailand", Coordinates(6.8675, 101.2501)),
        QiblaLocation("Yala", "Thailand", Coordinates(6.5411, 101.2804)),
        QiblaLocation("Narathiwat", "Thailand", Coordinates(6.4255, 101.8253)),
        QiblaLocation("Songkhla", "Thailand", Coordinates(7.1988, 100.5954)),
        QiblaLocation("Hat Yai", "Thailand", Coordinates(7.0084, 100.4767)),
        QiblaLocation("Bangkok", "Thailand", Coordinates(13.7563, 100.5018)),

        // Singapura
        QiblaLocation("Singapura", "Singapura", Coordinates(1.3521, 103.8198)),

        // Tanah Suci & Antarabangsa
        QiblaLocation("Makkah", "Arab Saudi", Coordinates(21.4225, 39.8262)),
        QiblaLocation("Madinah", "Arab Saudi", Coordinates(24.4672, 39.6111)),
        QiblaLocation("Baitulmaqdis", "Palestin", Coordinates(31.7683, 35.2137)),
        QiblaLocation("Dubai", "Emiriah Arab Bersatu", Coordinates(25.2048, 55.2708)),
        QiblaLocation("Kaherah", "Mesir", Coordinates(30.0444, 31.2357)),
        QiblaLocation("Istanbul", "Turki", Coordinates(41.0082, 28.9784)),
        QiblaLocation("London", "United Kingdom", Coordinates(51.5074, -0.1278)),
        QiblaLocation("New York", "Amerika Syarikat", Coordinates(40.7128, -74.0060)),
        QiblaLocation("Tokyo", "Jepun", Coordinates(35.6762, 139.6503)),
        QiblaLocation("Sydney", "Australia", Coordinates(-33.8688, 151.2093))
    )

    /**
     * Mengira bearing arah Kiblat (0-360 darjah mengikut arah jam dari Utara Benar)
     * dan jarak bulatan agung (great-circle distance) dalam kilometer ke Kaabah.
     */
    fun calculate(userLat: Double, userLon: Double): QiblaInfo {
        val lat1 = toRadians(userLat)
        val lon1 = toRadians(userLon)
        val lat2 = toRadians(KAABA_LATITUDE)
        val lon2 = toRadians(KAABA_LONGITUDE)

        val deltaLon = lon2 - lon1

        // Bearing bulatan agung
        val y = sin(deltaLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(deltaLon)
        val initialBearingRad = atan2(y, x)
        val bearingDeg = (toDegrees(initialBearingRad) + 360.0) % 360.0

        // Jarak Haversine
        val deltaLat = lat2 - lat1
        val a = sin(deltaLat / 2.0) * sin(deltaLat / 2.0) +
                cos(lat1) * cos(lat2) * sin(deltaLon / 2.0) * sin(deltaLon / 2.0)
        val c = 2.0 * atan2(sqrt(a), sqrt(1.0 - a))
        val distanceKm = EARTH_RADIUS_KM * c

        return QiblaInfo(
            qiblaBearing = bearingDeg.toFloat(),
            distanceKm = distanceKm,
            cardinalDirection = toCardinalDirection(bearingDeg.toFloat())
        )
    }

    /**
     * 16 arah mata angin dalam Bahasa Melayu / Nusantara
     * U (Utara), TL (Timur Laut), T (Timur), TG (Tenggara), S (Selatan), BD (Barat Daya), B (Barat), BL (Barat Laut)
     */
    fun toCardinalDirection(bearing: Float): String {
        val directions = arrayOf(
            "U", "U-TL", "TL", "T-TL",
            "T", "T-TG", "TG", "S-TG",
            "S", "S-BD", "BD", "B-BD",
            "B", "B-BL", "BL", "U-BL"
        )
        val index = (((bearing + 11.25f) % 360f) / 22.5f).toInt()
        return directions[index % 16]
    }

    private fun toRadians(degrees: Double): Double = degrees * (kotlin.math.PI / 180.0)
    private fun toDegrees(radians: Double): Double = radians * (180.0 / kotlin.math.PI)
}
