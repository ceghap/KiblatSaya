package com.kiblatsaya.app.core.haptics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType

class IosHapticFeedbackDriver : HapticFeedbackDriver {
    private val notificationGenerator = UINotificationFeedbackGenerator()
    private val impactGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)

    override fun performMilestonePulse() {
        notificationGenerator.prepare()
        notificationGenerator.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
    }

    override fun performLightClick() {
        impactGenerator.prepare()
        impactGenerator.impactOccurred()
    }
}

@Composable
actual fun rememberHapticFeedbackDriver(): HapticFeedbackDriver {
    return remember { IosHapticFeedbackDriver() }
}
