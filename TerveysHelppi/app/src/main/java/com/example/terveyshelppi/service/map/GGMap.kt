package com.example.terveyshelppi.service.map

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.terveyshelppi.R
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.libraries.maps.model.PolylineOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun MapGG(points: MutableList<LatLng>) {
    val tag = "terveyshelppi"

    val mapView = rememberMapViewWithLifecycle()

    // get screen size
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(dpValue.dp)
    ) {
        // use XML with compose
        AndroidView({ mapView }) { mapView ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val map = mapView.awaitMap()
                    map.uiSettings.isZoomControlsEnabled = true

                    val polylineOptions = PolylineOptions()
                    if (points.isNotEmpty()) {
                        Log.d(tag, "mapGG: with point $points")
                        val pickUp = points[0]
                        val destination = points[points.size - 1]
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 6f))
                        val markerOptions = MarkerOptions()
                            .title("Start point")
                            .position(pickUp)
                        map.addMarker(markerOptions)

                        val markerOptionsDestination = MarkerOptions()
                            .title("End point")
                            .position(destination)
                        map.addMarker(markerOptionsDestination)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 17F))

                        polylineOptions.add(pickUp)
                        for (index in points.indices) {
                            if (index != 0 && index != (points.size - 1))
                                polylineOptions.add(points[index])
                        }
                        polylineOptions.add(destination)
                        map.addPolyline(polylineOptions)
                    } else Log.d(tag, "mapGG: No data point")
                } catch (error: IOException) {
                    Log.d(tag, "mapGG: error is ${error.message}")
                }
            }
        }

    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }