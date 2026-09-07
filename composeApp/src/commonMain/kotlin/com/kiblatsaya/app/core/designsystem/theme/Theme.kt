package com.kiblatsaya.app.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.kiblatsaya.app.domain.theme.SpiritualPalette
import com.kiblatsaya.app.domain.theme.ThemeDisplayMode

fun createColorScheme(sanctuaryColors: SanctuaryColors, isDark: Boolean): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = sanctuaryColors.primary,
            onPrimary = sanctuaryColors.textPrimary,
            primaryContainer = sanctuaryColors.secondary,
            onPrimaryContainer = sanctuaryColors.primaryLight,
            secondary = sanctuaryColors.secondary,
            onSecondary = sanctuaryColors.textPrimary,
            secondaryContainer = sanctuaryColors.secondary.copy(alpha = 0.35f),
            onSecondaryContainer = sanctuaryColors.tertiary,
            tertiary = sanctuaryColors.tertiary,
            onTertiary = sanctuaryColors.surfaceBase,
            background = sanctuaryColors.surfaceBase,
            onBackground = sanctuaryColors.textPrimary,
            surface = sanctuaryColors.surfaceContainer,
            onSurface = sanctuaryColors.textPrimary,
            surfaceVariant = sanctuaryColors.surfaceElevated,
            onSurfaceVariant = sanctuaryColors.textSecondary,
            outline = sanctuaryColors.textTertiary,
            outlineVariant = sanctuaryColors.borderSubtle
        )
    } else {
        lightColorScheme(
            primary = sanctuaryColors.primary,
            onPrimary = Color.White,
            primaryContainer = sanctuaryColors.secondary.copy(alpha = 0.15f),
            onPrimaryContainer = sanctuaryColors.primary,
            secondary = sanctuaryColors.secondary,
            onSecondary = Color.White,
            secondaryContainer = sanctuaryColors.secondary.copy(alpha = 0.15f),
            onSecondaryContainer = sanctuaryColors.secondary,
            tertiary = sanctuaryColors.tertiary,
            onTertiary = Color.White,
            background = sanctuaryColors.surfaceBase,
            onBackground = sanctuaryColors.textPrimary,
            surface = sanctuaryColors.surfaceContainer,
            onSurface = sanctuaryColors.textPrimary,
            surfaceVariant = sanctuaryColors.surfaceElevated,
            onSurfaceVariant = sanctuaryColors.textSecondary,
            outline = sanctuaryColors.textTertiary,
            outlineVariant = sanctuaryColors.borderSubtle
        )
    }
}

@Composable
fun KiblatSayaTheme(
    palette: SpiritualPalette = SpiritualPalette.ZAMRUD,
    displayMode: ThemeDisplayMode = ThemeDisplayMode.GELAP,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isParchment = when (displayMode) {
        ThemeDisplayMode.GELAP -> false
        ThemeDisplayMode.CERAH -> true
        ThemeDisplayMode.SISTEM -> !isSystemDark
    }

    val sanctuaryColors = remember(palette, isParchment) {
        createSanctuaryColors(palette, isParchment)
    }

    CompositionLocalProvider(
        LocalSanctuaryColors provides sanctuaryColors,
        LocalSpiritualPalette provides palette,
        androidx.compose.material3.LocalContentColor provides sanctuaryColors.textPrimary
    ) {
        val colorScheme = remember(sanctuaryColors, isParchment) {
            createColorScheme(sanctuaryColors, isDark = !isParchment)
        }
        MaterialTheme(
            colorScheme = colorScheme,
            typography = KiblatSayaTypography,
            content = content
        )
    }
}
