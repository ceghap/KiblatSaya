package com.kiblatsaya.app.wear.presentation.qibla

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.kiblatsaya.app.domain.qibla.Coordinates
import com.kiblatsaya.app.domain.qibla.QiblaCalculator
import com.kiblatsaya.app.wear.hardware.rememberWearCompass
import com.kiblatsaya.app.wear.hardware.rememberWearHapticDriver
import com.kiblatsaya.app.wear.hardware.rememberWearLocation
import com.kiblatsaya.app.wear.presentation.theme.WearBorderFocus
import com.kiblatsaya.app.wear.presentation.theme.WearGoldAccent
import com.kiblatsaya.app.wear.presentation.theme.WearHeading
import com.kiblatsaya.app.wear.presentation.theme.WearLabelMicro
import com.kiblatsaya.app.wear.presentation.theme.WearPrimaryEmerald
import com.kiblatsaya.app.wear.presentation.theme.WearPrimaryLight
import com.kiblatsaya.app.wear.presentation.theme.WearSurfaceBase
import com.kiblatsaya.app.wear.presentation.theme.WearSurfaceContainer
import com.kiblatsaya.app.wear.presentation.theme.WearSurfaceElevated
import com.kiblatsaya.app.wear.presentation.theme.WearTextPrimary
import com.kiblatsaya.app.wear.presentation.theme.WearTextSecondary
import com.kiblatsaya.app.wear.presentation.theme.WearTextTertiary
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun WearQiblaScreen() {
    val compass = rememberWearCompass()
    val locationDriver = rememberWearLocation()
    val hapticDriver = rememberWearHapticDriver()

    val currentCoords = locationDriver.coordinates.value ?: Coordinates(3.1390, 101.6869) // Default KL
    val qiblaInfo = remember(currentCoords) {
        QiblaCalculator.calculate(currentCoords.latitude, currentCoords.longitude)
    }

    val currentAzimuth = compass.azimuth.value
    val relativeAngle = (qiblaInfo.qiblaBearing - currentAzimuth + 360f) % 360f
    val diffAngle = if (relativeAngle > 180f) relativeAngle - 360f else relativeAngle
    val isAligned = abs(diffAngle) < 3.5f

    var wasAligned by remember { mutableStateOf(false) }

    LaunchedEffect(isAligned) {
        if (isAligned && !wasAligned) {
            hapticDriver.performQiblaAligned()
        }
        wasAligned = isAligned
    }

    val needleColor by animateColorAsState(
        targetValue = if (isAligned) WearPrimaryLight else WearPrimaryEmerald,
        animationSpec = tween(durationMillis = 180)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WearSurfaceBase),
        contentAlignment = Alignment.Center
    ) {
        // Outer Compass Ring & 16 Cardinal marks
        Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            val strokeWidth = 2.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(
                color = if (isAligned) WearBorderFocus else WearSurfaceContainer,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // 16 Cardinal Ticks
            for (i in 0 until 16) {
                val angleRad = Math.toRadians((i * 22.5 - currentAzimuth).toDouble())
                val isMajor = i % 4 == 0
                val tickLength = if (isMajor) 8.dp.toPx() else 4.dp.toPx()
                val startX = center.x + (radius - tickLength) * Math.sin(angleRad).toFloat()
                val startY = center.y - (radius - tickLength) * Math.cos(angleRad).toFloat()
                val endX = center.x + radius * Math.sin(angleRad).toFloat()
                val endY = center.y - radius * Math.cos(angleRad).toFloat()

                drawLine(
                    color = if (isMajor) WearTextSecondary else WearTextTertiary,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = if (isMajor) 2.dp.toPx() else 1.dp.toPx()
                )
            }
        }

        // Qibla Pointer Needle
        Box(
            modifier = Modifier
                .size(130.dp)
                .rotate(relativeAngle),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)

                // North pointer arrow (Qibla)
                val needlePath = Path().apply {
                    moveTo(center.x, 8.dp.toPx())
                    lineTo(center.x + 8.dp.toPx(), center.y - 4.dp.toPx())
                    lineTo(center.x, center.y - 12.dp.toPx())
                    lineTo(center.x - 8.dp.toPx(), center.y - 4.dp.toPx())
                    close()
                }
                drawPath(path = needlePath, color = needleColor)

                // Tail (South)
                val tailPath = Path().apply {
                    moveTo(center.x, size.height - 8.dp.toPx())
                    lineTo(center.x + 6.dp.toPx(), center.y + 4.dp.toPx())
                    lineTo(center.x, center.y + 12.dp.toPx())
                    lineTo(center.x - 6.dp.toPx(), center.y + 4.dp.toPx())
                    close()
                }
                drawPath(path = tailPath, color = Color(0x33FFFFFF))
            }
        }

        // Center Kaaba Emblem
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isAligned) WearPrimaryEmerald else WearSurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🕋",
                fontSize = 15.sp
            )
        }

        // Top & Bottom Info Badges
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: Heading Status
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isAligned) "TEPAT KE KAABAH" else "${qiblaInfo.qiblaBearing.roundToInt()}° ${qiblaInfo.cardinalDirection}",
                    style = WearLabelMicro,
                    color = if (isAligned) WearPrimaryLight else WearTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bottom: Distance in KM
            Text(
                text = "${qiblaInfo.distanceKm.roundToInt()} km ke Makkah",
                style = WearLabelMicro.copy(fontSize = 9.sp),
                color = WearTextTertiary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}
