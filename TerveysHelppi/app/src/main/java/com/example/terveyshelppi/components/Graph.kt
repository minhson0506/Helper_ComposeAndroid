package com.example.terveyshelppi.components

import android.content.Context
import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TopAppBarWithBackButton
import com.example.terveyshelppi.ui.theme.background
import com.example.terveyshelppi.ui.theme.semibold
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate

// Graph to display Heart rate
@Composable
fun Graph(points: MutableList<Entry>, navController: NavController) {
    // get size of phone's screen
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
        Column {
            TopAppBarWithBackButton(navController = navController, id = R.string.heart)
            Text(
                stringResource(id = R.string.lineChart), color = Color.White,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontFamily = semibold, fontSize = 12.sp
            )
            // draw the graph with data
            AndroidView(
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp)
                    .fillMaxSize()
                    .height(dpValue.dp),
                factory = { context: Context ->
                    // init map with line chart
                    val view = LineChart(context)
                    view.legend.isEnabled = false
                    val data = LineData(LineDataSet(points, "BPM"))
                    val desc = Description()
                    desc.text = ""
                    // set color of data in graph
                    data.setValueTextColor(ColorTemplate.LIBERTY_COLORS[0])
                    view.xAxis.textColor = 0xffffff
                    view.legend.textColor = ColorTemplate.LIBERTY_COLORS[0]
                    desc.textColor = 0xffffff
                    view.axisLeft.textColor = ColorTemplate.LIBERTY_COLORS[0]
                    view.axisRight.textColor = ColorTemplate.LIBERTY_COLORS[0]
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
}


// bar graph to display daily activities
@Composable
fun GraphBarChart(points: MutableList<BarEntry>) {
    // get size of phone's screen
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 3
    // draw the graph with data
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpValue.dp),
        factory = { context: Context ->
            // init map with bar chart
            val view = BarChart(context)
            view.legend.isEnabled = false
            // set data for graph
            val entries: MutableList<BarEntry> = ArrayList()
            points.forEach {
                entries.add(it)
            }
            val set = BarDataSet(entries, "%")
            val data = BarData(set)
            data.setValueTextColor(ColorTemplate.LIBERTY_COLORS[0])
            view.data = data
            view.setDrawValueAboveBar(true)
            // set description of chart
            val desc = Description()
            desc.text = ""
            view.description = desc

            // set xAxis
            val xAxis = view.xAxis
            xAxis.textColor = 0xffffff

            // set color
            // set yAxis left
            val leftAxis = view.axisLeft
            leftAxis.textColor = ColorTemplate.LIBERTY_COLORS[0]

            // set yAxis right
            val rightAxis = view.axisRight
            rightAxis.textColor = ColorTemplate.LIBERTY_COLORS[0]

            view.setFitBars(true)
            view // return the view
        },
        update = { view ->
            // Update the view
            view.invalidate()
        }
    )
}
