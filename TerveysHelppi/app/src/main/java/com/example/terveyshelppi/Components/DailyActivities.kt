package com.example.terveyshelppi.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.Sensors.SensorViewModel
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.card
import com.example.terveyshelppi.ui.theme.regular
import com.example.terveyshelppi.ui.theme.semibold
import com.github.mikephil.charting.data.BarEntry

@Composable
fun DailyActivity(sensorModel: SensorViewModel, model: ResultViewModel) {
    val TAG = "terveyshelppi"

    var totalSteps by remember { mutableStateOf(100) }
    var targetSteps by remember { mutableStateOf(1000) }
    val data by model.getInfo().observeAsState()
    if (data != null) {
        targetSteps = data!!.targetSteps
        totalSteps = data!!.totalSteps.toInt()
    }

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color(0xFF321A54)
                    )
                )
            )
            .fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    stringResource(id = R.string.daily), color = Color.White, fontFamily = regular)
            }, backgroundColor = Color.Black,
            actions = {
                // Creating Icon button for dropdown menu
                Image(
                    painterResource(id = R.drawable.calendar),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(25.dp)
                        .clickable { //integrate calendar
                        }
                )
            })
        Column() {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        stringResource(id = R.string.today), color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                        fontFamily = semibold, fontSize = 16.sp
                    )
                    Log.d(TAG, "DailyActivity: targetSteps  $targetSteps")
                    Log.d(TAG, "DailyActivity: totalSteps  $totalSteps")
                    if (totalSteps != 0) {
                        Log.d(TAG, "DailyActivity: total steps in Composable $totalSteps type is ${totalSteps is Int}")
                        val points = mutableListOf(BarEntry(1.toFloat(),
                            totalSteps.toFloat() / targetSteps.toFloat()),
                            BarEntry(2.toFloat(), totalSteps.toFloat() / 2.toFloat() / targetSteps.toFloat()))
                        GraphBarChar(points = points)
                    }
                }
            }
        }
    }
}