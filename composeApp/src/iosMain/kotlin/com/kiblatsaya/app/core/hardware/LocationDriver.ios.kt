package com.kiblatsaya.app.core.hardware

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLLocationAccuracyHundredMeters
import platform.darwin.NSObject

class IosLocationProvider(
    private val onLocationReceived: (DeviceLocation) -> Unit
) : NSObject(), LocationProvider, CLLocationManagerDelegateProtocol {
    private val locationManager = CLLocationManager()
    private val geocoder = CLGeocoder()
    private var _isLocating by mutableStateOf(false)

    override val isLocating: Boolean
        get() = _isLocating

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyHundredMeters
    }

    override fun requestLocationUpdate() {
        _isLocating = true
        locationManager.requestWhenInUseAuthorization()
        locationManager.requestLocation()
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
        val lat = location.coordinate.latitude
        val lon = location.coordinate.longitude

        geocoder.reverseGeocodeLocation(location) { placemarks, _ ->
            val placemark = placemarks?.firstOrNull() as? CLPlacemark
            val cityName = placemark?.locality ?: placemark?.subAdministrativeArea ?: "Lokasi Semasa"
            val countryName = placemark?.country ?: ""
            _isLocating = false
            onLocationReceived(DeviceLocation(lat, lon, cityName, countryName))
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: platform.Foundation.NSError) {
        _isLocating = false
    }
}

@Composable
actual fun rememberLocationProvider(
    onLocationReceived: (DeviceLocation) -> Unit
): LocationProvider {
    return remember { IosLocationProvider(onLocationReceived) }
}
