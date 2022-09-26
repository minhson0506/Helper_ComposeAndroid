package com.example.terveyshelppi.Service

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import kotlin.math.round
import android.content.Context as Context1

class LocationService {
    @Composable
    fun getLocation(context: Context1, activity: Activity) {
        val TAG = "terveysheppi"
        var distance by remember {
            mutableStateOf(0.0)
        }
        var speed by remember {
            mutableStateOf(0.0)
        }
        var maxSpeed by remember {
            mutableStateOf(0.0)
        }
        var preLocation: Location? = null
        var long by remember {
            mutableStateOf(0.0)
        }
        var lat by remember {
            mutableStateOf(0.0)
        }

        var fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity)
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                preLocation = it
                Log.d(TAG,
                    "last location latitude: ${it?.latitude} and longitude: ${it?.longitude}")
            }
        }

        var locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult ?: return
                for (location in locationResult.locations) {
                    if (preLocation != null) {
                        distance = round(location.distanceTo(preLocation!!)).toDouble()
                    } else {
                        preLocation = location
                    }
                    speed = round(location.speed).toDouble()
                    if (speed > maxSpeed) maxSpeed = speed
                    Log.d(TAG, "distance is: $distance")
                    Log.d(TAG, "speed is: $speed")
                    Log.d(TAG,
                        "location latitude: ${location.latitude} and longitude ${location.longitude}")
                    //preLocation = location
                    long = location.longitude
                    lat = location.latitude
                }
            }
        }

        fun startGetLocation() {
            val locationRequest = LocationRequest.create().setInterval(1000)
                .setPriority(PRIORITY_HIGH_ACCURACY)
            fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
        }

        fun stopGetLocation() {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}