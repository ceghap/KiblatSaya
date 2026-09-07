package com.kiblatsaya.app

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.russhwolf.settings.Settings
import com.kiblatsaya.app.core.designsystem.theme.KiblatSayaTheme
import com.kiblatsaya.app.domain.theme.SpiritualPalette
import com.kiblatsaya.app.domain.theme.ThemeDisplayMode
import com.kiblatsaya.app.domain.theme.ThemePreferences
import com.kiblatsaya.app.presentation.qibla.QiblaScreen
import com.kiblatsaya.app.presentation.splash.SplashScreen

@Composable
fun App() {
    val settings = remember { Settings() }
    var currentPalette by remember { mutableStateOf(ThemePreferences.getPalette(settings)) }
    var currentDisplayMode by remember { mutableStateOf(ThemePreferences.getDisplayMode(settings)) }
    var showSplash by remember { mutableStateOf(true) }

    KiblatSayaTheme(
        palette = currentPalette,
        displayMode = currentDisplayMode
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Crossfade(
                targetState = showSplash,
                animationSpec = tween(durationMillis = 350),
                label = "SplashToCompassTransition"
            ) { isSplash ->
                if (isSplash) {
                    SplashScreen(
                        onSplashFinished = { showSplash = false }
                    )
                } else {
                    QiblaScreen(
                        currentPalette = currentPalette,
                        currentDisplayMode = currentDisplayMode,
                        onSelectPalette = { palette ->
                            currentPalette = palette
                            ThemePreferences.setPalette(settings, palette)
                        },
                        onSelectDisplayMode = { mode ->
                            currentDisplayMode = mode
                            ThemePreferences.setDisplayMode(settings, mode)
                        }
                    )
                }
            }
        }
    }
}
