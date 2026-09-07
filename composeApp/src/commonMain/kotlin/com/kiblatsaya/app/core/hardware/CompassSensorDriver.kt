package com.kiblatsaya.app.core.hardware

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.StateFlow

expect class CompassSensorDriver {
    val heading: StateFlow<Float>
    val isSensorAvailable: Boolean
    val accuracy: StateFlow<Int>
    fun start()
    fun stop()
}

@Composable
expect fun rememberCompassSensorDriver(): CompassSensorDriver
