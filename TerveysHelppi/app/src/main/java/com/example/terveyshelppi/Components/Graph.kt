package com.example.terveyshelppi.Components

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun Graph(points: MutableList<Entry>) {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().getDisplayMetrics().heightPixels / screenPixelDensity
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .height(dpValue.dp),
        factory = { context: Context ->
            val view = LineChart(context)
            view.legend.isEnabled = false
            val data = LineData(LineDataSet(points, "BPM"))
            val desc = Description()
            desc.text = "Beats Per Minute"
            view.description = desc
            view.data = data
            view // return the view
        },
        update = { view ->
            // Update the view
            view.invalidate()
        }
    )
}