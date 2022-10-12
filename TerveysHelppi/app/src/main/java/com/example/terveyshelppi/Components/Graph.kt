package com.example.terveyshelppi.Components

import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.terveyshelppi.ui.theme.background
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.BarEntry
import com.madrapps.plot.line.DataPoint
import com.madrapps.plot.line.LineGraph
import com.madrapps.plot.line.LinePlot
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition

@Composable
fun SampleLineGraph(lines: List<List<DataPoint>>) {
    LineGraph(
        plot = LinePlot(
            listOf(
                LinePlot.Line(
                    lines[0],
                    LinePlot.Connection(color = Color.Red),
                    LinePlot.Intersection(color = Color.Green),
                    LinePlot.Highlight(color = Color.Yellow),
                )
            ),
            xAxis = LinePlot.XAxis(steps = 1, unit = 1f, roundToInt = true),
            yAxis = LinePlot.YAxis(steps = 1, roundToInt = false),
            grid = LinePlot.Grid(Color.Black, steps = 2),
        ),
        modifier = Modifier.fillMaxSize(),
        onSelection = { xLine, points ->
            // Do whatever you want here
        }
    )
}



//@Composable
//fun GraphLineWithPlot(lines: List<Pair<Int, Int>>) {
//    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
//    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity
//    AndroidView(
//        modifier = Modifier
//            .fillMaxSize()
//            .height(dpValue.dp),
//        factory = { context: Context ->
//            val view = XYPlot(context, "Heart rate")
//            val domainLabels = lines.map { it.first }
//            val series = SimpleXYSeries(lines.map { it.second }, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "BPM")
//            val seriesFormat = LineAndPointFormatter(context, R.id.line_and_point)
//            seriesFormat.interpolationParams =
//                CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal)
//
//            view.addSeries(series, seriesFormat)
//            view.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object :Format() {
//                override fun format(
//                    obj: Any,
//                    toAppendTo: StringBuffer,
//                    pos: FieldPosition?,
//                ): StringBuffer? {
//                    val i = Math.round((obj as Number).toFloat())
//                    return toAppendTo.append(domainLabels.get(i))
//                }
//
//                override fun parseObject(source: String?, pos: ParsePosition?): Any? {
//                    return null
//                }
//            }
//
////            val view = LineChart(context)
////            view.legend.isEnabled = false
////            val data = LineData(LineDataSet(points, "BPM"))
////            val desc = Description()
////            desc.text = "Beats Per Minute"
////            view.xAxis.textColor = (0xffffff)
////            view.legend.textColor = (0xffffff)
////            desc.textColor = (0xffffff)
////            view.axisLeft.textColor = (0xffffff)
////            view.description = desc
////            view.data = data
//            view // return the view
//        },
//        update = { view ->
//            // Update the view
//            view.invalidate()
//        }
//    )
//}



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
fun GraphBarChar(points: MutableList<BarEntry>) {
    val screenPixelDensity = LocalContext.current.resources.displayMetrics.density
    val dpValue = Resources.getSystem().displayMetrics.heightPixels / screenPixelDensity / 2.5
    Log.d("terveyshelppi", "DailyActivity: start draw graph")

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(dpValue.dp),
        factory = { context: Context ->
            val view = BarChart(context)
            view.legend.isEnabled = false
            val entries: MutableList<BarEntry> = ArrayList()
            points.forEach {
                if (it != null) {
                    entries.add(it)
                }
            }
            val data = BarData(BarDataSet(entries, "%"))
            data.setValueTextColor(0xffffff)
            val desc = Description()
            desc.text = ""
            view.description = desc
            view.data = data
            view.setFitBars(true)
            view // return the view
        },
        update = { view ->
            // Update the view
            view.invalidate()
        }
    )
}