package com.kiblatsaya.app.wear

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kiblatsaya.app.wear.presentation.qibla.WearQiblaScreen
import com.kiblatsaya.app.wear.presentation.theme.WearSanctuaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            WearSanctuaryTheme {
                WearQiblaScreen()
            }
        }
    }
}
