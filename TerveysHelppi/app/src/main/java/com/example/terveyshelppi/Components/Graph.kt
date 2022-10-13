package com.example.terveyshelppi.Components

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.background
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.libraries.maps.MapView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate


@Composable
fun Graph(points: MutableList<Entry>) {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        background
                    )
                )
            )
            .fillMaxSize()
    ) {
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
                view.xAxis.textColor = (0xffffff)
                view.legend.textColor = (0xffffff)
                desc.textColor = (0xffffff)
                view.axisLeft.textColor = (0xffffff)
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
}

@Composable
fun GraphMulti(firstPoints: MutableList<Entry>, secondPoints: MutableList<Entry>) {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .height(dpValue.dp),
        factory = { context: Context ->
            val view = LineChart(context)
            view.legend.isEnabled = false
            val firstData = LineDataSet(firstPoints, "BPM")
            val secondData = LineDataSet(secondPoints, "extra")
            val dataSets: List<ILineDataSet> = listOf(firstData, secondData)
            val data = LineData(dataSets)
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

@Composable
fun GraphBarChart(points: MutableList<BarEntry>) {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 3
    Log.d("terveyshelppi", "DailyActivity: start draw graph")

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpValue.dp),
        factory = { context: Context ->
            val view = BarChart(context)
            view.legend.isEnabled = false
            // set data for graph
            val entries: MutableList<BarEntry> = ArrayList()
            points.forEach {
                if (it != null) {
                    entries.add(it)
                }
            }
            val set = BarDataSet(entries, "%")

            set.colors = mutableListOf(ColorTemplate.COLORFUL_COLORS[0], ColorTemplate.COLORFUL_COLORS[1], ColorTemplate.COLORFUL_COLORS[2])
            val data = BarData(set)
            data.setValueTextColor(ColorTemplate.COLORFUL_COLORS[3])

            view.data = data
            view.setDrawValueAboveBar(true)

            // set xAxis
            val xAxis = view.xAxis
            xAxis.textColor = ColorTemplate.COLORFUL_COLORS[4]


            // set color
            var colorInt = listOf(0xffffff, 0xff0000).toIntArray()
            // set yAxis left
            val leftAxis = view.axisLeft
            val color = ColorTemplate.createColors(colorInt)
            leftAxis.textColor = color[0]

            // set yAxis right
            val rightAxis = view.axisRight
            rightAxis.textColor = color[1]

            view.setFitBars(true)
            view // return the view
        },
        update = { view ->
            // Update the view
            view.invalidate()
        }
    )
}
