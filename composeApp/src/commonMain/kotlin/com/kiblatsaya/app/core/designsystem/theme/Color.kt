package com.kiblatsaya.app.core.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.kiblatsaya.app.domain.theme.SpiritualPalette

data class SanctuaryColors(
    val surfaceBase: Color,
    val surfaceContainer: Color,
    val surfaceElevated: Color,
    val surfaceBright: Color,
    val surfaceContainerLowest: Color,
    val primary: Color,
    val primaryLight: Color,
    val secondary: Color,
    val tertiary: Color,
    val jewel: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val borderSubtle: Color,
    val borderVerySubtle: Color,
    val borderFocus: Color,
    val glow: Color,
    val isParchment: Boolean
)

private data class PaletteTokens(
    val primary: Color,
    val primaryLight: Color,
    val secondary: Color,
    val tertiary: Color,
    val jewel: Color
)

fun createSanctuaryColors(
    palette: SpiritualPalette,
    isParchment: Boolean
): SanctuaryColors {
    val tokens = when (palette) {
        SpiritualPalette.ZAMRUD -> PaletteTokens(
            primary = Color(0xFF2D7A5D),
            primaryLight = Color(0xFF8AD6B4),
            secondary = Color(0xFF1E5F48),
            tertiary = Color(0xFF8EAFA0),
            jewel = Color(0xFF064E3B)
        )
        SpiritualPalette.NILAM -> PaletteTokens(
            primary = Color(0xFF2A5B84),
            primaryLight = Color(0xFF7EAFDE),
            secondary = Color(0xFF1A3B58),
            tertiary = Color(0xFF8AA8C7),
            jewel = Color(0xFF0C243B)
        )
        SpiritualPalette.EMAS -> PaletteTokens(
            primary = Color(0xFF9A6B24),
            primaryLight = Color(0xFFE5B869),
            secondary = Color(0xFF5C3E14),
            tertiary = Color(0xFFC7A87A),
            jewel = Color(0xFF3B2706)
        )
        SpiritualPalette.DELIMA -> PaletteTokens(
            primary = Color(0xFF8B3A5A),
            primaryLight = Color(0xFFD48BA7),
            secondary = Color(0xFF5A243A),
            tertiary = Color(0xFFBA8A9D),
            jewel = Color(0xFF3B1424)
        )
    }

    return if (isParchment) {
        SanctuaryColors(
            surfaceBase = Color(0xFFF8F9FA),
            surfaceContainer = Color(0xFFFFFFFF),
            surfaceElevated = Color(0xFFF1F3F5),
            surfaceBright = Color(0xFFFFFFFF),
            surfaceContainerLowest = Color(0xFFECEEF1),
            primary = when (palette) {
                SpiritualPalette.ZAMRUD -> Color(0xFF1B6D4C)
                SpiritualPalette.NILAM -> Color(0xFF1A5280)
                SpiritualPalette.EMAS -> Color(0xFF8C5D17)
                SpiritualPalette.DELIMA -> Color(0xFF8A2E4B)
            },
            primaryLight = when (palette) {
                SpiritualPalette.ZAMRUD -> Color(0xFF14543A)
                SpiritualPalette.NILAM -> Color(0xFF133F63)
                SpiritualPalette.EMAS -> Color(0xFF6B450E)
                SpiritualPalette.DELIMA -> Color(0xFF6B2238)
            },
            secondary = tokens.secondary,
            tertiary = tokens.tertiary,
            jewel = Color(0xFFE8F5EE),
            textPrimary = Color(0xFF111827),
            textSecondary = Color(0xFF4B5563),
            textTertiary = Color(0xFF9CA3AF),
            borderSubtle = Color(0x1A000000),
            borderVerySubtle = Color(0x0D000000),
            borderFocus = tokens.primary.copy(alpha = 0.50f),
            glow = tokens.primary.copy(alpha = 0.15f),
            isParchment = true
        )
    } else {
        SanctuaryColors(
            surfaceBase = Color(0xFF121316),
            surfaceContainer = Color(0xFF181A1E),
            surfaceElevated = Color(0xFF202329),
            surfaceBright = Color(0xFF38393C),
            surfaceContainerLowest = Color(0xFF0D0E11),
            primary = tokens.primary,
            primaryLight = tokens.primaryLight,
            secondary = tokens.secondary,
            tertiary = tokens.tertiary,
            jewel = tokens.jewel,
            textPrimary = Color(0xFFF4F5F6),
            textSecondary = Color(0xFF9DA3AE),
            textTertiary = Color(0xFF646A76),
            borderSubtle = Color(0x1FFFFFFF),
            borderVerySubtle = Color(0x10FFFFFF),
            borderFocus = tokens.primary.copy(alpha = 0.40f),
            glow = tokens.primary.copy(alpha = 0.25f),
            isParchment = false
        )
    }
}

val LocalSanctuaryColors = staticCompositionLocalOf {
    createSanctuaryColors(SpiritualPalette.ZAMRUD, isParchment = false)
}

val LocalSpiritualPalette = staticCompositionLocalOf {
    SpiritualPalette.ZAMRUD
}
