package com.kiblatsaya.app.core.hardware

import androidx.compose.runtime.Composable

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val countryName: String
)

interface LocationProvider {
    val isLocating: Boolean
    fun requestLocationUpdate()
}

@Composable
expect fun rememberLocationProvider(
    onLocationReceived: (DeviceLocation) -> Unit
): LocationProvider
