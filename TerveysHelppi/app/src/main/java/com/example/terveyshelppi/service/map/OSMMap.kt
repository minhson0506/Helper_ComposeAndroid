package com.example.terveyshelppi.service.map

import android.content.Context
import android.content.res.Resources
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terveyshelppi.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.io.IOException
import java.util.*

fun getAddress(context: Context, lat: Double, long: Double): String {
    val tag = "terveyshelppi"

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
        Log.d(tag, "getAddress: not response")
    }
    return address
}

@Composable
fun ShowPoints(geoPoint: GeoPoint, address: String) {
    val tag = "terveyshelppi"

    val map = composeMap()
    val mapInitialized by remember(map) { mutableStateOf(false) }
    if (!mapInitialized) {
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.controller.setZoom(17.0)
        map.controller.setCenter(GeoPoint(60.17, 24.95))
    }

    val marker = Marker(map)

    // get screen size to set height of map
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 3

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpValue.dp),
        factory = { map }) {
        try {
            it.controller.setCenter(geoPoint)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.position = geoPoint
            marker.title = address
            map.overlays.clear()
            map.overlays.add(marker)
        } catch (error: IOException) {
            Log.d(tag, "mapGG: error is ${error.message}")
        }
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