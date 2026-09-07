package com.kiblatsaya.app.core.hardware

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLHeading
import platform.darwin.NSObject

actual class CompassSensorDriver : NSObject(), CLLocationManagerDelegateProtocol {
    private val locationManager = CLLocationManager()

    actual val isSensorAvailable: Boolean = CLLocationManager.headingAvailable()

    private val _heading = MutableStateFlow(0f)
    actual val heading: StateFlow<Float> = _heading.asStateFlow()

    private val _accuracy = MutableStateFlow(0)
    actual val accuracy: StateFlow<Int> = _accuracy.asStateFlow()

    private var currentHeadingFiltered = 0f

    init {
        locationManager.delegate = this
        locationManager.headingFilter = 0.5
    }

    actual fun start() {
        if (isSensorAvailable) {
            locationManager.startUpdatingHeading()
        }
    }

    actual fun stop() {
        if (isSensorAvailable) {
            locationManager.stopUpdatingHeading()
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateHeading: CLHeading) {
        val trueHeading = if (didUpdateHeading.trueHeading >= 0) {
            didUpdateHeading.trueHeading.toFloat()
        } else {
            didUpdateHeading.magneticHeading.toFloat()
        }

        val diff = ((trueHeading - currentHeadingFiltered + 540f) % 360f) - 180f
        currentHeadingFiltered = (currentHeadingFiltered + diff * 0.2f + 360f) % 360f
        _heading.value = currentHeadingFiltered
    }
}

@Composable
actual fun rememberCompassSensorDriver(): CompassSensorDriver {
    val driver = remember { CompassSensorDriver() }

    DisposableEffect(driver) {
        driver.start()
        onDispose {
            driver.stop()
        }
    }

    return driver
}
