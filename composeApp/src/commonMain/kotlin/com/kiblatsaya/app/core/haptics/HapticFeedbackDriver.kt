package com.kiblatsaya.app.core.haptics

import androidx.compose.runtime.Composable

interface HapticFeedbackDriver {
    fun performMilestonePulse()
    fun performLightClick()
}

@Composable
expect fun rememberHapticFeedbackDriver(): HapticFeedbackDriver
