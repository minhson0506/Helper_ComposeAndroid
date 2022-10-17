package com.example.terveyshelppi.service.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import android.content.Context
import androidx.compose.runtime.livedata.observeAsState
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.service.roomDB.UserData
import com.google.android.libraries.maps.model.LatLng

@SuppressLint("MissingPermission")
@Composable
fun GetLocation(context: Context, activity: Activity, model: ResultViewModel) {
    val tag = "terveysheppi"
    Log.d(tag, "GetLocation: start to get location")

    var firstTime = 0
    var preLocation: Location? = null

    // user data from Room
    val userData by model.getInfo().observeAsState()

    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
        PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            preLocation = it
            Log.d(
                tag,
                "last location latitude: ${it?.latitude} and longitude: ${it?.longitude}"
            )
        }
    }

    // callback function
    val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            Log.d(tag, "onLocationResult: $locationResult")
            for (location in locationResult.locations) {
                // calculator distance when user has previous location
                if (preLocation != null) {
                    if (model.distance.value != null)
                        model.distance.postValue(location.distanceTo(preLocation) + model.distance.value!!)
                    else model.distance.postValue(location.distanceTo(preLocation).toDouble())
                    if (model.recording.value == true) {
                        Log.d(tag, "onLocationResult: post location when recording")
                        model.points.value?.add(LatLng(location.latitude, location.longitude))
                        if (model.distanceRecording.value != null)
                            model.distanceRecording.postValue(location.distanceTo(preLocation) + model.distanceRecording.value!!)
                        else model.distanceRecording.postValue(
                            location.distanceTo(preLocation).toDouble()
                        )
                    }
                    preLocation = location
                    Log.d(tag, "onLocationResult: model distance data in LocationService $userData")

                    // update total distance of user to Room
                    if (userData != null) {
                        val user = model.distance.value?.let {
                            UserData(
                                userData!!.name,
                                userData!!.weight,
                                userData!!.height,
                                userData!!.targetSteps,
                                userData!!.targetCals,
                                userData!!.targetHours,
                                userData!!.heartRate,
                                it.toInt(),
                                userData!!.totalCalories,
                                userData!!.totalSteps,
                                userData!!.totalHours,
                                userData!!.avatar,
                                userData!!.stepBeginOfDay,
                                userData!!.day
                            )
                        }
                        if (user != null) {
                            model.updateInfo(user)
                        }
                    }

                } else {
                    preLocation = location
                }
                Log.d(
                    tag,
                    "onLocationResult: speed is ${location.speed} and ${location.speed.toDouble()}"
                )
                if (model.recording.value == true) model.speed.postValue(location.speed * 3.6)
                else model.speed.postValue(0.0)
                Log.d(
                    tag,
                    "location latitude: ${location.latitude} and longitude ${location.longitude}}"
                )

                //post location
                model.long.postValue(location.longitude)
                model.lat.postValue(location.latitude)
                if (firstTime == 0 || model.firstAltitude.value == 0.0) {
                    model.firstAltitude.postValue(location.altitude)
                    model.secondAltitude.postValue(location.altitude)
                    firstTime++
                } else if (model.recording.value == true) model.secondAltitude.postValue(location.altitude)
            }
        }
    }

    fun startGetLocation() {
        val locationRequest = LocationRequest.create().setInterval(1000)
            .setPriority(PRIORITY_HIGH_ACCURACY)
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    startGetLocation()
}