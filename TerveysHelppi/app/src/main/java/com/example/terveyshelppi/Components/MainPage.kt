package com.example.terveyshelppi.Components

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
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.ui.theme.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(
    navController: NavController,
    model: ResultViewModel,
) {
    val TAG = "terveyshelppi"

    var user by remember { mutableStateOf("") }
    var totalSteps by remember { mutableStateOf("") }
    var totalCalories by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf("") }
    var targetSteps by remember { mutableStateOf("") }
    var targetHours by remember { mutableStateOf("") }
    var targetCals by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }

    val data = model.getInfo().observeAsState()

    val heartRate by model.mBPM.observeAsState()
    val highHeartRate by model.highmBPM.observeAsState()
    val lowHeartRate by model.lowmBPM.observeAsState()
    val temp by model.tempValue.observeAsState()

    Log.d(TAG, "MainPage: temperature = $temp")

    if (data.value != null) {
        user = data.value?.name.toString()
        totalSteps = data.value!!.totalSteps.toInt().toString()
        totalCalories = data.value!!.totalCalories.toString()
        totalHours = data.value!!.totalHours.toString()
        weight = data.value!!.weight.toString()
        height = data.value!!.height.toString()
        targetSteps = data.value!!.targetSteps.toString()
        targetCals = data.value!!.targetCals.toString()
        targetHours = data.value!!.targetHours.toString()
    }

    Log.d(TAG, "MainPage: userinfo $data")

    val textArray = listOf(
        Triple(R.drawable.step, totalSteps, targetSteps),
        Triple(R.drawable.cal, totalCalories, targetCals),
        Triple(R.drawable.clock, totalHours, targetHours),
    )

    //get time of day
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
    var text by remember { mutableStateOf("") }

    text = when (timeOfDay) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..23 -> "Good Night"
        else -> "Hello"
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
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                modifier = Modifier.padding(top = 30.dp, start = 20.dp, bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row() {
                    Text(
                        text,
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
                        if (text == "Good Morning") painterResource(id = R.drawable.sun) else painterResource(
                            id = R.drawable.evening
                        ),
                        "",
                        modifier = Modifier
                            .padding(top = 5.dp, start = 10.dp)
                            .size(20.dp)
                    )
                }
//                temp?.let {
//                    Text(
//                        it.substringAfterLast(".") + " °C", color = Color.White,
//                        modifier = Modifier.padding(end = 20.dp),
//                        fontFamily = semibold,
//                        fontSize = 16.sp
//                    )
//                }
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
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)) {
                        Text(
                            stringResource(id = R.string.daily), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        LazyVerticalGrid(
                            cells = GridCells.Fixed(3),
                            modifier = Modifier.padding(
                                top = 20.dp,
                                start = 10.dp,
                                end = 10.dp
                            )
                        ) {
                            items(textArray) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                        },
                                        modifier = Modifier.size(45.dp),
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = button),
                                    ) {
                                        Image(
                                            painterResource(id = it.first),
                                            "",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Text(
                                        "${it.second} / ${it.third}",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontFamily = semibold,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                }
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
                            stringResource(id = R.string.exercise), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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
                                    painterResource(id = R.drawable.start),
                                    "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                stringResource(id = R.string.start_ex),
                                color = Color.White,
                                fontFamily = semibold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(end = 10.dp)
                            )
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
                            Text(
                                stringResource(id = R.string.history), color = Color.White,
                                fontFamily = semibold, fontSize = 14.sp
                            )
                        }
                    }

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 40.dp)) {
                        Text(
                            stringResource(id = R.string.body),
                            color = Color.White,
                            fontFamily = semibold,
                            fontSize = 16.sp,
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                    },
                                    modifier = Modifier.size(45.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = button),
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.scale),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    if (weight != "") weight + " " + stringResource(id = R.string.kg) else "-- " + stringResource(
                                        id = R.string.kg
                                    ),
                                    color = Color.White,
                                    fontFamily = semibold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                    },
                                    modifier = Modifier
                                        .size(45.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = button),
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.height),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    if (height != "") height + " " + stringResource(id = R.string.cm) else "-- " + stringResource(
                                        id = R.string.cm
                                    ),
                                    color = Color.White,
                                    fontFamily = semibold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                            }
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
                            .padding(start = 10.dp, end = 25.dp, bottom = 20.dp)
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
//                            Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                },
                                modifier = Modifier
                                    .size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.heart),
                                    "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(
                                heartRate.toString() + stringResource(id = R.string.bpm),
                                color = Color.White,
                                fontFamily = semibold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 10.dp)
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
                            /*}
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
                            }*/
                        }

                    }
                }
            }
        }
    }
}
