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
import com.example.terveyshelppi.ui.theme.*
import com.github.mikephil.charting.data.BarEntry

@Composable
fun DailyActivity(model: ResultViewModel) {
    val TAG = "terveyshelppi"

    var totalSteps by remember { mutableStateOf(100) }
    var totalCals by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf("") }
    var targetSteps by remember { mutableStateOf(1000) }
    val data by model.getInfo().observeAsState()
    if (data != null) {
        targetSteps = data!!.targetSteps
        totalSteps = data!!.totalSteps.toInt()
        totalCals = data!!.totalCalories.toString()
        totalHours = data!!.totalHours.toString()
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
                    stringResource(id = R.string.daily), color = Color.White, fontFamily = regular
                )
            }, backgroundColor = Color.Black,
            actions = {
                // Creating Icon button for dropdown menu
                Image(
                    painterResource(id = R.drawable.calendar),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .clickable { //integrate calendar
                        }
                )
            })
        Column(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 20.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly
        ) {
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
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                        fontFamily = semibold, fontSize = 16.sp
                    )
                    if (totalSteps != 0) {
                        val points = mutableListOf(
                            BarEntry(
                                1.toFloat(),
                                totalSteps.toFloat() / targetSteps.toFloat()
                            ),
                            BarEntry(
                                2.toFloat(),
                                totalSteps.toFloat() / 2.toFloat() / targetSteps.toFloat()
                            )
                        )
                        GraphBarChar(points = points)
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(bottom = 10.dp, top = 30.dp, start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            stringResource(id = R.string.steps),
                            color = smallText,
                            fontSize = 15.sp,
                            fontFamily = regular
                        )
                        Text(
                            stringResource(id = R.string.activecal),
                            color = smallText,
                            fontSize = 15.sp,
                            fontFamily = regular
                        )
                        Text(
                            stringResource(id = R.string.activeTime),
                            color = smallText,
                            fontSize = 15.sp,
                            fontFamily = regular
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(bottom = 10.dp, start = 25.dp, end = 25.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painterResource(id = R.drawable.step),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.cal),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                        Image(
                            painterResource(id = R.drawable.clock),
                            "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            totalSteps.toString(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = semibold
                        )
                        Text(
                            totalCals,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = semibold
                        )
                        Text(
                            totalHours,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = semibold
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp, start = 15.dp, end = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(id = R.string.distance), color = Color.White,
                            fontFamily = regular, fontSize = 14.sp
                        )
                        Text(
                            "-- km", color = Color.White,
                            fontFamily = semibold, fontSize = 14.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(id = R.string.total_cal), color = Color.White,
                            fontFamily = regular, fontSize = 14.sp
                        )
                        Text(
                            "-- Cal", color = Color.White,
                            fontFamily = semibold, fontSize = 14.sp
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(bottom = 20.dp, start = 15.dp, end = 15.dp)) {
                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(id = R.string.running), color = Color.White,
                            fontFamily = regular, fontSize = 16.sp
                        )
                        Text(
                            "09:30", color = smallText,
                            fontFamily = regular, fontSize = 14.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row() {
                            Image(
                                painterResource(id = R.drawable.timer),
                                "",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "01:20:09", color = Color.White,
                                fontFamily = semibold, fontSize = 16.sp,
                                modifier = Modifier.padding(start = 5 .dp)
                            )
                        }
                        Row() {
                            Image(
                                painterResource(id = R.drawable.distance),
                                "",
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "5.04" + " km", color = Color.White,
                                fontFamily = semibold, fontSize = 16.sp,
                                modifier = Modifier.padding(start = 5 .dp)
                            )
                        }

                    }
                }
            }
        }
    }
}