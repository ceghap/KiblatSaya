package com.kiblatsaya.app.wear.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors

val WearSurfaceBase = Color(0xFF000000)
val WearSurfaceContainer = Color(0xFF141619)
val WearSurfaceElevated = Color(0xFF1E2126)
val WearPrimaryEmerald = Color(0xFF2D7A5D)
val WearPrimaryLight = Color(0xFF8AD6B4)
val WearGoldAccent = Color(0xFFE5B869)
val WearBorderFocus = Color(0xFF3B9B78)

val WearTextPrimary = Color(0xFFF4F5F6)
val WearTextSecondary = Color(0xFF9DA3AE)
val WearTextTertiary = Color(0xFF646A76)

val WearHeading = TextStyle(
    fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    color = WearTextPrimary
)

val WearLabelMicro = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.SemiBold,
    fontSize = 10.sp,
    letterSpacing = 1.sp,
    color = WearTextSecondary
)

@Composable
fun WearSanctuaryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = Colors(
            primary = WearPrimaryEmerald,
            primaryVariant = WearPrimaryLight,
            secondary = WearGoldAccent,
            background = WearSurfaceBase,
            surface = WearSurfaceContainer,
            onPrimary = Color.White,
            onBackground = WearTextPrimary,
            onSurface = WearTextPrimary
        ),
        content = content
    )
}
