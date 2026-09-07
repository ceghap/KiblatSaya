package com.kiblatsaya.app.wear.hardware

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kiblatsaya.app.domain.qibla.Coordinates

class WearCompassDriver(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val rotationSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        ?: sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)

    private val _azimuth = mutableFloatStateOf(0f)
    val azimuth: State<Float> = _azimuth

    private val _accuracy = mutableStateOf("GOOD")
    val accuracy: State<String> = _accuracy

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private var isListening = false
    private var lastAzimuth = 0f

    fun startListening() {
        if (isListening || rotationSensor == null) return
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        isListening = true
    }

    fun stopListening() {
        if (!isListening) return
        sensorManager.unregisterListener(this)
        isListening = false
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val degrees = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            val normalized = (degrees + 360f) % 360f

            // Exponential low-pass filter to eliminate wrist trembling jitter
            val diff = (normalized - lastAzimuth + 180f + 360f) % 360f - 180f
            val smoothed = (lastAzimuth + diff * 0.25f + 360f) % 360f
            lastAzimuth = smoothed
            _azimuth.floatValue = smoothed
        } else if (event.sensor.type == Sensor.TYPE_ORIENTATION) {
            val normalized = (event.values[0] + 360f) % 360f
            _azimuth.floatValue = normalized
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        _accuracy.value = when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "GOOD"
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "CALIBRATE"
            SensorManager.SENSOR_STATUS_UNRELIABLE -> "UNRELIABLE"
            else -> "GOOD"
        }
    }
}

class WearLocationDriver(private val context: Context) : LocationListener {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val _coordinates = mutableStateOf<Coordinates?>(null)
    val coordinates: State<Coordinates?> = _coordinates

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        try {
            val lastGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val lastNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val best = lastGps ?: lastNet
            if (best != null) {
                _coordinates.value = Coordinates(best.latitude, best.longitude)
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10f, this)
            } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10f, this)
            }
        } catch (_: Exception) {}
    }

    fun stopLocationUpdates() {
        try {
            locationManager.removeUpdates(this)
        } catch (_: Exception) {}
    }

    override fun onLocationChanged(location: Location) {
        _coordinates.value = Coordinates(location.latitude, location.longitude)
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    @Deprecated("Deprecated in Java")
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
}

@Composable
fun rememberWearCompass(): WearCompassDriver {
    val context = LocalContext.current
    val compass = remember(context) { WearCompassDriver(context.applicationContext) }
    DisposableEffect(compass) {
        compass.startListening()
        onDispose {
            compass.stopListening()
        }
    }
    return compass
}

@Composable
fun rememberWearLocation(): WearLocationDriver {
    val context = LocalContext.current
    val locationDriver = remember(context) { WearLocationDriver(context.applicationContext) }
    DisposableEffect(locationDriver) {
        locationDriver.requestLocationUpdates()
        onDispose {
            locationDriver.stopLocationUpdates()
        }
    }
    return locationDriver
}
