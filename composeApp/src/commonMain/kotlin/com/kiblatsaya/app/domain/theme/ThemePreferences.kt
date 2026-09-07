package com.kiblatsaya.app.domain.theme

import androidx.compose.ui.graphics.Color
import com.russhwolf.settings.Settings

enum class SpiritualPalette(
    val titleMalay: String,
    val previewColor: Color
) {
    ZAMRUD("Zamrud (Hijau)", Color(0xFF2D7A5D)),
    NILAM("Nilam (Biru)", Color(0xFF2A5B84)),
    EMAS("Emas (Keemasan)", Color(0xFF9A6B24)),
    DELIMA("Delima (Merah Hati)", Color(0xFF8B3A5A))
}

enum class ThemeDisplayMode(
    val titleMalay: String
) {
    GELAP("Obsidian (Gelap)"),
    CERAH("Parchment (Cerah)"),
    SISTEM("Ikut Tetapan Sistem")
}

object ThemePreferences {
    private const val KEY_PALETTE = "kiblat_theme_palette"
    private const val KEY_DISPLAY_MODE = "kiblat_theme_display_mode"

    fun getPalette(settings: Settings): SpiritualPalette {
        val saved = settings.getStringOrNull(KEY_PALETTE) ?: return SpiritualPalette.ZAMRUD
        return try {
            SpiritualPalette.valueOf(saved)
        } catch (_: Exception) {
            SpiritualPalette.ZAMRUD
        }
    }

    fun setPalette(settings: Settings, palette: SpiritualPalette) {
        settings.putString(KEY_PALETTE, palette.name)
    }

    fun getDisplayMode(settings: Settings): ThemeDisplayMode {
        val saved = settings.getStringOrNull(KEY_DISPLAY_MODE) ?: return ThemeDisplayMode.GELAP
        return try {
            ThemeDisplayMode.valueOf(saved)
        } catch (_: Exception) {
            ThemeDisplayMode.GELAP
        }
    }

    fun setDisplayMode(settings: Settings, mode: ThemeDisplayMode) {
        settings.putString(KEY_DISPLAY_MODE, mode.name)
    }
}
