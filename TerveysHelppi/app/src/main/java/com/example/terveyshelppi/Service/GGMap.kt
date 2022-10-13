package com.example.terveyshelppi.Service

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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

@Composable
fun mapGG(points: MutableList<LatLng>) {
    val mapView = rememberMapViewWithLifecycle()
    val TAG = "terveyshelppi"

    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 3

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(dpValue.dp)
    ) {
        AndroidView({ mapView }) { mapView ->
            CoroutineScope(Dispatchers.Main).launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true

//                val pickUp =  LatLng(-35.016, 143.321)
                val polylineOptions = PolylineOptions()
                if (points.isNotEmpty()) {
                    Log.d(TAG, "mapGG: with point $points")
                    val pickUp = points[0]
//                val destination = LatLng(-32.491, 147.309)
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

                    polylineOptions.add(pickUp)
                    for (index in points.indices) {
                        if (index != 0 && index != (points.size - 1))
                            polylineOptions.add(points[index])
                    }
                    polylineOptions.add(destination)

                } else Log.d(TAG, "mapGG: No data point")

                map.addPolyline(polylineOptions)
//                map.addPolyline(PolylineOptions().add( pickUp,
//                    LatLng(-34.747, 145.592),
//                    LatLng(-34.364, 147.891),
//                    LatLng(-33.501, 150.217),
//                    LatLng(-32.306, 149.248),
//                    destination))

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