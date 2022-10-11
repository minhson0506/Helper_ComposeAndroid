package com.example.terveyshelppi.Service

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Transformations.map
import com.example.terveyshelppi.R
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.round
import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import java.util.*

fun GetLocation(context: Context, activity: Activity, model: ResultViewModel) {
    val TAG = "terveysheppi"
    Log.d(TAG, "GetLocation: start to get location")

    var firstTime = 0
//    val distance: Double? by model.distance.observeAsState(0.0)
//    val speed: Double? by model.speed.observeAsState(0.0)
//    val maxSpeed: Double? by model.maxSpeed.observeAsState(0.0)
    var preLocation: Location? = null
//    var long by remember { mutableStateOf(0.0) }
//    var lat by remember { mutableStateOf(0.0) }
//    val long: Double? by model.long.observeAsState(null)
//    val lat: Double? by model.lat.observeAsState(null)

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
            Log.d(TAG, "onLocationResult: $locationResult")
            for (location in locationResult.locations) {
                if (preLocation != null) {
                    if (model.distance.value != null)
                        model.distance.postValue(location.distanceTo(preLocation) + model.distance.value!!)
                    else model.distance.postValue(location.distanceTo(preLocation).toDouble())
                    if (model.recording.value == true) {
                        if (model.distanceRecording.value != null)
                            model.distanceRecording.postValue(location.distanceTo(preLocation) + model.distance.value!!)
                        else model.distanceRecording.postValue(location.distanceTo(preLocation).toDouble())
                    }
                    preLocation = location
                } else {
                    preLocation = location
                }
                Log.d(TAG, "onLocationResult: speed is ${location.speed} and ${location.speed.toDouble()}")
//                speed = round(location.speed).toDouble()
                if (model.recording.value == true) model.speed.postValue(location.speed * 3.6)
                else model.speed.postValue(0.0)

//                if (location.speed.toDouble() > model.maxSpeed.value!!) model.maxSpeed.postValue(
//                    location.speed.toDouble())
//                Log.d(TAG, "distance is: $distance")
//                Log.d(TAG, "speed is: $speed")
                Log.d(TAG,
                    "location latitude: ${location.latitude} and longitude ${location.longitude} type is ${(location.latitude) is Double}")

                //post location
                model.long.postValue(location.longitude)
                model.lat.postValue(location.latitude)
                if (firstTime == 0) {
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
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    fun stopGetLocation() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    startGetLocation()
//    if (lat != 0.0 && long != 0.0) showPoint(geoPoint = GeoPoint(lat, long),
//        address = getAddress(context = context, lat, long))
}

fun getAddress(context: Context, lat: Double, long: Double): String {
    var TAG = "w2_d3_LocationMap"
    val geocoder = Geocoder(context, Locale.getDefault())
    var address = ""
    try {
        address = if (Build.VERSION.SDK_INT >= 33) {
            val location = geocoder.getFromLocation(lat, long, 1)
            location.first().getAddressLine(0)
        } else {
            geocoder.getFromLocation(lat, long, 1)?.first()?.getAddressLine(0) ?: ""
        }
    } catch (e: Error) {
        Log.d(TAG, "getAddress: not response")
    }
    return address
}

@Composable
fun showPoint(geoPoint: GeoPoint, address: String) {
    val map = composeMap()
    var mapInitialized by remember(map) { mutableStateOf(false) }
    if (!mapInitialized) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(17.0)
        map.controller.setCenter(GeoPoint(60.17, 24.95))
        mapInitialized = true
    }

    val marker = Marker(map)

    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().getDisplayMetrics().heightPixels / screenPixelDensity / 2.5

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpValue.dp),
        factory = { map }) {
        address ?: return@AndroidView
        it.controller.setCenter(geoPoint)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.position = geoPoint
        marker.showInfoWindow()
        marker.title = address + "; lat ${geoPoint.latitude} & long ${geoPoint.longitude}"
        map.overlays.clear()
        map.overlays.add(marker)
        map.invalidate()
    }
}

@Composable
fun composeMap(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply { id = R.id.map }
    }
    return mapView
}