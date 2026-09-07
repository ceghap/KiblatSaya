package com.kiblatsaya.app.domain.qibla

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QiblaCalculatorTest {

    @Test
    fun testKualaLumpurQiblaBearing() {
        // Kuala Lumpur: 3.1390° N, 101.6869° E
        // Bearing Kiblat dari KL ke Kaabah secara purata ialah sekitar 292.9° (Barat-Barat Laut)
        val info = QiblaCalculator.calculate(3.1390, 101.6869)
        assertTrue(abs(info.qiblaBearing - 292.9f) < 1.0f, "KL bearing ${info.qiblaBearing} should be around 292.9°")
        assertTrue(info.distanceKm > 6900 && info.distanceKm < 7200, "Distance from KL to Kaaba should be around ~7050km")
        assertEquals("B-BL", info.cardinalDirection)
    }

    @Test
    fun testJakartaQiblaBearing() {
        // Jakarta: -6.2088° S, 106.8456° E
        // Bearing Kiblat dari Jakarta ke Kaabah sekitar 295.1°
        val info = QiblaCalculator.calculate(-6.2088, 106.8456)
        assertTrue(abs(info.qiblaBearing - 295.1f) < 1.0f, "Jakarta bearing ${info.qiblaBearing} should be around 295.1°")
        assertTrue(info.distanceKm > 7800 && info.distanceKm < 8100, "Distance should be ~7900km")
    }

    @Test
    fun testBandarSeriBegawanQiblaBearing() {
        // Bandar Seri Begawan: 4.9031° N, 114.9398° E
        val info = QiblaCalculator.calculate(4.9031, 114.9398)
        assertTrue(abs(info.qiblaBearing - 290.7f) < 1.5f, "BSB bearing ${info.qiblaBearing} should be around 290.7°")
    }

    @Test
    fun testLondonQiblaBearing() {
        // London: 51.5074° N, -0.1278° W
        // London ke Mekah adalah arah Tenggara (~118.9°)
        val info = QiblaCalculator.calculate(51.5074, -0.1278)
        assertTrue(abs(info.qiblaBearing - 118.9f) < 1.0f, "London bearing ${info.qiblaBearing} should be around 118.9°")
        assertEquals("T-TG", info.cardinalDirection)
    }

    @Test
    fun testCardinalDirectionConversion() {
        assertEquals("U", QiblaCalculator.toCardinalDirection(0f))
        assertEquals("U", QiblaCalculator.toCardinalDirection(360f))
        assertEquals("T", QiblaCalculator.toCardinalDirection(90f))
        assertEquals("S", QiblaCalculator.toCardinalDirection(180f))
        assertEquals("B", QiblaCalculator.toCardinalDirection(270f))
    }
}
