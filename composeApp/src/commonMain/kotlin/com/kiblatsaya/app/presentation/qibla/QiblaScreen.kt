package com.kiblatsaya.app.presentation.qibla

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.kiblatsaya.app.core.designsystem.theme.BodyMedium
import com.kiblatsaya.app.core.designsystem.theme.BodySmall
import com.kiblatsaya.app.core.designsystem.theme.HeadlineLargeSerif
import com.kiblatsaya.app.core.designsystem.theme.LabelSmallCaps
import com.kiblatsaya.app.core.designsystem.theme.LocalSanctuaryColors
import com.kiblatsaya.app.core.designsystem.theme.TitleMediumSerif
import com.kiblatsaya.app.core.haptics.rememberHapticFeedbackDriver
import com.kiblatsaya.app.core.hardware.rememberCompassSensorDriver
import com.kiblatsaya.app.core.hardware.rememberLocationProvider
import com.kiblatsaya.app.domain.qibla.Coordinates
import com.kiblatsaya.app.domain.qibla.QiblaCalculator
import com.kiblatsaya.app.domain.qibla.QiblaLocation
import com.kiblatsaya.app.domain.theme.SpiritualPalette
import com.kiblatsaya.app.domain.theme.ThemeDisplayMode
import com.kiblatsaya.app.presentation.theme.ThemeSelectionBottomSheet
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    currentPalette: SpiritualPalette,
    currentDisplayMode: ThemeDisplayMode,
    onSelectPalette: (SpiritualPalette) -> Unit,
    onSelectDisplayMode: (ThemeDisplayMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val sanctuaryColors = LocalSanctuaryColors.current
    val compassDriver = rememberCompassSensorDriver()
    val rawHeading by compassDriver.heading.collectAsState()
    val sensorAccuracy by compassDriver.accuracy.collectAsState()
    val isLowAccuracy = sensorAccuracy == 0 || sensorAccuracy == 1
    val hapticDriver = rememberHapticFeedbackDriver()
    val settings = remember { Settings() }

    var isGpsLocation by remember {
        mutableStateOf(settings.getBoolean("kiblat_is_gps", false))
    }

    var selectedLocation by remember {
        val savedCity = settings.getStringOrNull("kiblat_selected_city")
        val savedLat = settings.getDoubleOrNull("kiblat_selected_lat")
        val savedLon = settings.getDoubleOrNull("kiblat_selected_lon")
        val savedCountry = settings.getStringOrNull("kiblat_selected_country") ?: "Malaysia"

        if (savedCity != null && savedLat != null && savedLon != null) {
            mutableStateOf(QiblaLocation(savedCity, savedCountry, Coordinates(savedLat, savedLon)))
        } else {
            val found = QiblaCalculator.DEFAULT_LOCATIONS.find { it.name == savedCity }
            mutableStateOf(found ?: QiblaCalculator.DEFAULT_LOCATIONS.first())
        }
    }

    val locationProvider = rememberLocationProvider { deviceLocation ->
        val loc = QiblaLocation(
            name = deviceLocation.cityName,
            country = deviceLocation.countryName,
            coordinates = Coordinates(deviceLocation.latitude, deviceLocation.longitude)
        )
        selectedLocation = loc
        isGpsLocation = true
        settings.putString("kiblat_selected_city", loc.name)
        settings.putString("kiblat_selected_country", loc.country)
        settings.putDouble("kiblat_selected_lat", loc.coordinates.latitude)
        settings.putDouble("kiblat_selected_lon", loc.coordinates.longitude)
        settings.putBoolean("kiblat_is_gps", true)
    }

    // Permintaan kemaskini lokasi GPS secara automatik semasa mula buka aplikasi
    LaunchedEffect(Unit) {
        locationProvider.requestLocationUpdate()
    }

    var showLocationPicker by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    val qiblaInfo = remember(selectedLocation) {
        QiblaCalculator.calculate(
            selectedLocation.coordinates.latitude,
            selectedLocation.coordinates.longitude
        )
    }

    // Putaran kompas lancar tanpa lonjakan pada sempadan 0/360 darjah
    var continuousHeading by remember { mutableStateOf(rawHeading) }
    LaunchedEffect(rawHeading) {
        val diff = ((rawHeading - continuousHeading + 540f) % 360f) - 180f
        continuousHeading += diff
    }

    val animatedCompassRotation by animateFloatAsState(
        targetValue = -continuousHeading,
        animationSpec = tween(durationMillis = 160, easing = LinearEasing),
        label = "compassRotation"
    )

    // Perbezaan sudut antara orientasi peranti dan arah kiblat (-180..+180)
    val angleDiff = remember(rawHeading, qiblaInfo.qiblaBearing) {
        ((qiblaInfo.qiblaBearing - rawHeading + 540f) % 360f) - 180f
    }
    val isAligned = abs(angleDiff) <= 3.5f

    // Maklum balas haptik apabila tepat menghadap Kiblat
    var wasAligned by remember { mutableStateOf(false) }
    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            hapticDriver.performMilestonePulse()
        }
        wasAligned = isAligned
    }

    val alignmentGlowColor by animateColorAsState(
        targetValue = if (isAligned) sanctuaryColors.primaryLight else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "alignmentGlow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(sanctuaryColors.surfaceBase)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bar Tajuk Atas dengan Butang Tema & GPS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showThemePicker = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(sanctuaryColors.surfaceElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Tukar Tema",
                        tint = sanctuaryColors.primaryLight,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "KiblatSaya",
                        style = HeadlineLargeSerif.copy(color = sanctuaryColors.textPrimary, fontSize = 24.sp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Arah ke Kaabah Suci, Makkah",
                        style = BodySmall.copy(color = sanctuaryColors.textSecondary, fontSize = 11.sp)
                    )
                }

                IconButton(
                    onClick = { locationProvider.requestLocationUpdate() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(sanctuaryColors.surfaceElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Kemaskini Lokasi GPS",
                        tint = sanctuaryColors.primaryLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Butang Pil Pemilih Lokasi / Penunjuk GPS
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(sanctuaryColors.surfaceElevated)
                    .clickable { showLocationPicker = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (locationProvider.isLocating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(13.dp),
                        color = sanctuaryColors.primaryLight,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mencari GPS...",
                        style = LabelSmallCaps.copy(color = sanctuaryColors.textPrimary, fontSize = 11.sp)
                    )
                } else {
                    Icon(
                        imageVector = if (isGpsLocation) Icons.Default.MyLocation else Icons.Default.LocationOn,
                        contentDescription = "Lokasi",
                        tint = sanctuaryColors.primaryLight,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val locationText = if (selectedLocation.country.isNotBlank()) {
                        "${selectedLocation.name}, ${selectedLocation.country}"
                    } else {
                        selectedLocation.name
                    }
                    Text(
                        text = locationText,
                        style = LabelSmallCaps.copy(color = sanctuaryColors.textPrimary, fontSize = 11.sp)
                    )
                    if (isGpsLocation) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(sanctuaryColors.primary.copy(alpha = 0.35f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "GPS",
                                style = LabelSmallCaps.copy(
                                    color = sanctuaryColors.primaryLight,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Cakera Kompas Utama
            Box(
                modifier = Modifier
                    .size(290.dp)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Gelang Cahaya Keserasian Luar
                Box(
                    modifier = Modifier
                        .size(266.dp)
                        .clip(CircleShape)
                        .background(alignmentGlowColor.copy(alpha = 0.08f))
                        .border(
                            width = if (isAligned) 2.dp else 1.dp,
                            color = if (isAligned) sanctuaryColors.primaryLight.copy(alpha = 0.6f) else sanctuaryColors.borderSubtle,
                            shape = CircleShape
                        )
                )

                // Cakera Kompas Berputar
                Canvas(
                    modifier = Modifier
                        .size(250.dp)
                        .rotate(animatedCompassRotation)
                ) {
                    val radius = size.minDimension / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Plat bulat latar belakang
                    drawCircle(
                        color = sanctuaryColors.surfaceContainer,
                        radius = radius,
                        center = center
                    )

                    // Senggatan tanda darjah
                    for (degree in 0 until 360 step 5) {
                        val isMajor = degree % 30 == 0
                        val isCardinal = degree % 90 == 0
                        val tickLength = if (isCardinal) 14.dp.toPx() else if (isMajor) 9.dp.toPx() else 5.dp.toPx()
                        val tickWidth = if (isCardinal) 2.5.dp.toPx() else if (isMajor) 1.5.dp.toPx() else 1.dp.toPx()
                        val tickColor = if (isCardinal) sanctuaryColors.textPrimary else if (isMajor) sanctuaryColors.textSecondary else sanctuaryColors.textTertiary.copy(alpha = 0.4f)

                        rotate(degrees = degree.toFloat(), pivot = center) {
                            drawLine(
                                color = tickColor,
                                start = Offset(center.x, center.y - radius + 6.dp.toPx()),
                                end = Offset(center.x, center.y - radius + 6.dp.toPx() + tickLength),
                                strokeWidth = tickWidth
                            )
                        }
                    }

                    // Jarum Utara Benar (Merah)
                    val northPath = Path().apply {
                        moveTo(center.x, center.y - radius + 22.dp.toPx())
                        lineTo(center.x - 7.dp.toPx(), center.y - 30.dp.toPx())
                        lineTo(center.x + 7.dp.toPx(), center.y - 30.dp.toPx())
                        close()
                    }
                    drawPath(northPath, color = Color(0xFFE53935))

                    // Jarum Selatan
                    val southPath = Path().apply {
                        moveTo(center.x, center.y + radius - 22.dp.toPx())
                        lineTo(center.x - 7.dp.toPx(), center.y + 30.dp.toPx())
                        lineTo(center.x + 7.dp.toPx(), center.y + 30.dp.toPx())
                        close()
                    }
                    drawPath(southPath, color = sanctuaryColors.textTertiary.copy(alpha = 0.5f))

                    // Jarum Penunjuk Kiblat (Menuju ke Kaabah)
                    rotate(degrees = qiblaInfo.qiblaBearing, pivot = center) {
                        val qiblaNeedle = Path().apply {
                            moveTo(center.x, center.y - radius + 8.dp.toPx())
                            lineTo(center.x - 10.dp.toPx(), center.y - 45.dp.toPx())
                            lineTo(center.x + 10.dp.toPx(), center.y - 45.dp.toPx())
                            close()
                        }
                        drawPath(
                            qiblaNeedle,
                            brush = Brush.verticalGradient(
                                colors = listOf(sanctuaryColors.primaryLight, sanctuaryColors.primary),
                                startY = center.y - radius,
                                endY = center.y - 45.dp.toPx()
                            )
                        )

                        // Titik penanda sudut Kaabah
                        drawCircle(
                            color = sanctuaryColors.primaryLight,
                            radius = 6.dp.toPx(),
                            center = Offset(center.x, center.y - radius + 8.dp.toPx())
                        )
                    }

                    // Gelang sempadan dalam cakera
                    drawCircle(
                        color = sanctuaryColors.borderSubtle,
                        radius = radius - 26.dp.toPx(),
                        center = center,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Penunjuk Arah Hadapan Telefon (Jam 12)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.TopCenter)
                        .padding(top = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Hadapan Peranti",
                        tint = if (isAligned) sanctuaryColors.primaryLight else sanctuaryColors.textPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Lambang Kaabah & Status Tengah
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(sanctuaryColors.surfaceElevated)
                        .border(
                            width = 2.dp,
                            color = if (isAligned) sanctuaryColors.primaryLight else sanctuaryColors.borderSubtle,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isAligned) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Tepat",
                                tint = sanctuaryColors.primaryLight,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "TEPAT",
                                style = LabelSmallCaps.copy(
                                    color = sanctuaryColors.primaryLight,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        } else {
                            // Simbol kiub Kaabah dengan pita emas
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF141518))
                                    .border(1.dp, sanctuaryColors.primary, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .background(Color(0xFFE2B755))
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${qiblaInfo.qiblaBearing.roundToInt()}°",
                                style = LabelSmallCaps.copy(
                                    color = sanctuaryColors.textPrimary,
                                    fontSize = 11.sp,
                                    fontFeatureSettings = "tnum"
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Amaran Kalibrasi Kompas jika ketepatan sensor rendah
            AnimatedVisibility(
                visible = isLowAccuracy,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE5A83B).copy(alpha = 0.12f))
                        .border(1.dp, Color(0xFFE5A83B).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Kalibrasi",
                        tint = Color(0xFFE5A83B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Gerakkan telefon bentuk angka 8 (∞) untuk kalibrasi kompas",
                        style = LabelSmallCaps.copy(
                            color = Color(0xFFE5A83B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Sepanduk Panduan Keselarasan Dinamik
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isAligned) sanctuaryColors.primary.copy(alpha = 0.2f) else sanctuaryColors.surfaceContainer)
                    .border(
                        width = 1.dp,
                        color = if (isAligned) sanctuaryColors.borderFocus else sanctuaryColors.borderSubtle,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isAligned) {
                    Text(
                        text = "Menghadap Kaabah Suci",
                        style = BodyMedium.copy(
                            color = sanctuaryColors.primaryLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                } else {
                    val turnDirection = if (angleDiff > 0) "kanan" else "kiri"
                    val degreesToTurn = abs(angleDiff).roundToInt()
                    Text(
                        text = "Pusing $degreesToTurn° ke $turnDirection anda",
                        style = BodyMedium.copy(
                            color = sanctuaryColors.textSecondary,
                            fontFeatureSettings = "tnum"
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Ringkasan Maklumat (Arah, Kiblat, Jarak)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(sanctuaryColors.surfaceContainer)
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetricColumn(
                    label = "ARAH",
                    value = "${((continuousHeading % 360f + 360f) % 360f).roundToInt()}°",
                    subValue = QiblaCalculator.toCardinalDirection(((continuousHeading % 360f + 360f) % 360f)),
                    valueColor = sanctuaryColors.textPrimary
                )

                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(sanctuaryColors.borderSubtle)
                )

                MetricColumn(
                    label = "KIBLAT",
                    value = "${qiblaInfo.qiblaBearing.roundToInt()}°",
                    subValue = qiblaInfo.cardinalDirection,
                    valueColor = sanctuaryColors.primaryLight
                )

                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .width(1.dp)
                        .background(sanctuaryColors.borderSubtle)
                )

                MetricColumn(
                    label = "JARAK",
                    value = "${qiblaInfo.distanceKm.roundToInt()}",
                    subValue = "km",
                    valueColor = sanctuaryColors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Helaian Pemilih Tema
        if (showThemePicker) {
            ThemeSelectionBottomSheet(
                currentPalette = currentPalette,
                currentDisplayMode = currentDisplayMode,
                onSelectPalette = onSelectPalette,
                onSelectDisplayMode = onSelectDisplayMode,
                onDismiss = { showThemePicker = false }
            )
        }

        // Helaian Pemilih Lokasi Serantau
        if (showLocationPicker) {
            LocationPickerBottomSheet(
                currentLocation = selectedLocation,
                onRequestGpsLocation = {
                    locationProvider.requestLocationUpdate()
                },
                onSelectLocation = { loc ->
                    selectedLocation = loc
                    isGpsLocation = false
                    settings.putString("kiblat_selected_city", loc.name)
                    settings.putString("kiblat_selected_country", loc.country)
                    settings.putDouble("kiblat_selected_lat", loc.coordinates.latitude)
                    settings.putDouble("kiblat_selected_lon", loc.coordinates.longitude)
                    settings.putBoolean("kiblat_is_gps", false)
                    showLocationPicker = false
                },
                onDismiss = { showLocationPicker = false }
            )
        }
    }
}

@Composable
private fun MetricColumn(
    label: String,
    value: String,
    subValue: String,
    valueColor: Color
) {
    val sanctuaryColors = LocalSanctuaryColors.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = LabelSmallCaps.copy(color = sanctuaryColors.textTertiary, fontSize = 9.sp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = value,
                style = TitleMediumSerif.copy(
                    color = valueColor,
                    fontSize = 18.sp,
                    fontFeatureSettings = "tnum"
                )
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = subValue,
                style = LabelSmallCaps.copy(
                    color = sanctuaryColors.textSecondary,
                    fontSize = 10.sp,
                    fontFeatureSettings = "tnum"
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationPickerBottomSheet(
    currentLocation: QiblaLocation,
    onRequestGpsLocation: () -> Unit,
    onSelectLocation: (QiblaLocation) -> Unit,
    onDismiss: () -> Unit
) {
    val sanctuaryColors = LocalSanctuaryColors.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    val filteredLocations = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QiblaCalculator.DEFAULT_LOCATIONS
        } else {
            QiblaCalculator.DEFAULT_LOCATIONS.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.country.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = sanctuaryColors.surfaceContainer,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pilih Lokasi",
                    style = TitleMediumSerif.copy(color = sanctuaryColors.textPrimary, fontSize = 20.sp)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Tutup",
                        tint = sanctuaryColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Butang Tindakan: Kemaskini Lokasi via GPS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(sanctuaryColors.primary.copy(alpha = 0.2f))
                    .border(1.dp, sanctuaryColors.primary.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .clickable {
                        onRequestGpsLocation()
                        onDismiss()
                    }
                    .padding(horizontal = 16.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "GPS",
                    tint = sanctuaryColors.primaryLight,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kemaskini Lokasi Melalui GPS",
                    style = BodyMedium.copy(
                        color = sanctuaryColors.primaryLight,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Kotak Carian Bandar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari bandar atau negara...", style = BodyMedium.copy(color = sanctuaryColors.textTertiary)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = sanctuaryColors.textTertiary)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = sanctuaryColors.primary,
                    unfocusedBorderColor = sanctuaryColors.borderSubtle,
                    focusedTextColor = sanctuaryColors.textPrimary,
                    unfocusedTextColor = sanctuaryColors.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredLocations) { loc ->
                    val isSelected = loc.name == currentLocation.name && loc.country == currentLocation.country
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) sanctuaryColors.primary.copy(alpha = 0.15f) else sanctuaryColors.surfaceElevated)
                            .clickable { onSelectLocation(loc) }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = loc.name,
                                style = BodyMedium.copy(
                                    color = if (isSelected) sanctuaryColors.primaryLight else sanctuaryColors.textPrimary,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            )
                            Text(
                                text = loc.country,
                                style = BodySmall.copy(color = sanctuaryColors.textSecondary)
                            )
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Dipilih",
                                tint = sanctuaryColors.primaryLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
