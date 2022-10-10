package com.example.terveyshelppi.Components

import android.app.Application
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.Sensors.SensorViewModel
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.*

@Composable
fun MainPage(
    application: Application,
    navController: NavController,
    model: ResultViewModel,
    sensorViewModel: SensorViewModel
) {
    val TAG = "terveyshelppi"

    var user by remember { mutableStateOf("") }
    var totalSteps by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val viewModel = ResultViewModel(application)
    val sensorModel = SensorViewModel()
    val data = viewModel.getInfo().observeAsState()

    val heartRate by model.mBPM.observeAsState()
    val highHeartRate by model.highmBPM.observeAsState()
    val lowHeartRate by model.lowmBPM.observeAsState()

    val graph by model.graph.observeAsState()
    val graphMulti by model.testGraphMulti.observeAsState()
    val barGraph by model.barGraph.observeAsState()
    val temp by sensorViewModel.tempValue.observeAsState()

    Log.d(TAG, "MainPage: temperature = $temp")

    if (data.value != null) {
        user = data.value?.name.toString()
        totalSteps = data.value!!.totalSteps.toString()
        totalCalories = data.value!!.totalCalories.toString()
        totalHours = data.value!!.totalHours.toString()
        weight = data.value!!.weight.toString()
        height = data.value!!.height.toString()
    }

    Log.d(TAG, "MainPage: userinfo $data")

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
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                modifier = Modifier.padding(top = 30.dp, start = 20.dp, bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Text(
                        stringResource(id = R.string.gm),
                        color = Color.White,
                        fontFamily = regular,
                        fontSize = 20.sp
                    )
                    Text(
                        if (user != "") user else stringResource(id = R.string.username),
                        color = Color.White,
                        modifier = Modifier.padding(start = 5.dp),
                        fontFamily = semibold,
                        fontSize = 20.sp
                    )

                    Image(
                        painterResource(id = R.drawable.sun),
                        "",
                        modifier = Modifier
                            .padding(top = 5.dp, start = 10.dp)
                            .size(20.dp)
                    )
                }
                temp?.let {
                    Text(
                        it.substringAfterLast(".") + " °C", color = Color.White,
                        modifier = Modifier.padding(end = 20.dp),
                        fontFamily = semibold,
                        fontSize = 16.sp
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .clickable {
                            navController.navigate("daily")
                        },
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.daily), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
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
                                .padding(bottom = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                totalSteps.substringAfterLast("."),
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                            Text(
                                totalCalories,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                            Text(
                                totalHours,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.exercise), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("exercise")
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.walk),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.run),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bike),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.menu),
                                    "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.body),
                            color = Color.White,
                            fontFamily = semibold,
                            fontSize = 16.sp,
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                if (weight != "") weight + " " + stringResource(id = R.string.kg) else "-- " + stringResource(
                                    id = R.string.kg
                                ), color = Color.White,
                                fontFamily = semibold, fontSize = 14.sp
                            )
                            Text(
                                if (height != "") height + " " + stringResource(id = R.string.cm) else "-- " + stringResource(
                                    id = R.string.cm
                                ), color = Color.White,
                                fontFamily = semibold, fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(start = 40.dp)
                            )
                        }
                    }

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clickable {
                            navController.navigate("graph-heartRate")
                        },
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, bottom = 20.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.heart), color = Color.White,
                            modifier = Modifier.padding(top = 20.dp),
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row() {
                                Text(
                                    heartRate.toString() + stringResource(id = R.string.bpm),
                                    color = Color.White,
                                    fontFamily = semibold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = if (highHeartRate == 0) "Highest: --" else "Highest: $highHeartRate",
                                    color = Color.White,
                                    fontFamily = semibold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                                Text(
                                    text = if (lowHeartRate == 300) "Lowest: --" else "Lowest: $lowHeartRate",
                                    color = Color.White,
                                    fontFamily = semibold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    model.highmBPM.postValue(0)
                                    model.lowmBPM.postValue(300)
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                                contentPadding = PaddingValues(7.dp),
                                modifier = Modifier.defaultMinSize(
                                    minWidth = 1.dp,
                                    minHeight = 1.dp
                                )
                            ) {
                                Text(
                                    stringResource(id = R.string.Reset),
                                    color = Color.White,
                                    fontFamily = regular,
                                    fontSize = 14.sp,
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
