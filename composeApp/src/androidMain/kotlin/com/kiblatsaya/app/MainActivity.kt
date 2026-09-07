package com.kiblatsaya.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Standard Android 15 edge-to-edge initialization
        enableEdgeToEdge()

        setContent {
            App()
        }
    }
}
