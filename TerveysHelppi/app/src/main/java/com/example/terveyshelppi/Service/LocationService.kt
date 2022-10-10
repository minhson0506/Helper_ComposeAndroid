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
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terveyshelppi.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.*

class LocationService {
    val TAG = "terveysheppi"
    @Composable
    fun getLocation(context: Context, activity: Activity) {
        var distance by remember { mutableStateOf(0.0) }
        var speed by remember { mutableStateOf(0.0) }
        var maxSpeed by remember { mutableStateOf(0.0) }
        var preLocation: Location? = null
        var long by remember { mutableStateOf(0.0) }
        var lat by remember { mutableStateOf(0.0) }

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

        startGetLocation()
        if (lat != 0.0 && long != 0.0) showPoint(geoPoint = GeoPoint(lat, long),
            address = getAddress(context = context, lat, long))
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

        AndroidView(factory = { map }) {
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
}