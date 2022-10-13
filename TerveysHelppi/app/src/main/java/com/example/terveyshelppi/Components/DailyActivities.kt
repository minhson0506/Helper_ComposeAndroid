package com.example.terveyshelppi.Components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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
import com.example.terveyshelppi.Service.RoomDB.ExerciseData
import com.example.terveyshelppi.ui.theme.*
import com.github.mikephil.charting.data.BarEntry
import java.util.Calendar
import androidx.compose.foundation.lazy.items


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyActivity(model: ResultViewModel, navController: NavController) {
    val TAG = "terveyshelppi"
    val time = Calendar.getInstance().time.toString().slice(0..10)

    val exerciseData by model.getAllExercises().observeAsState()
    val list = mutableListOf(0L)
    val listExercise = mutableListOf<ExerciseData>()

    exerciseData?.forEach {
        list.add(it.activeTime)
        if(time == it.timeStart.slice(0..10)) {
            listExercise.add(it)
        }
    }

    val distance: Double by model.distance.observeAsState(0.0)
    var totalSteps by remember { mutableStateOf(0) }
    var totalCals by remember { mutableStateOf(0) }
    var totalHours by remember { mutableStateOf(0) }
    var targetSteps by remember { mutableStateOf("") }
    var targetCals by remember { mutableStateOf("") }
    var targetHours by remember { mutableStateOf("") }
    var beginSteps by remember { mutableStateOf(0) }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }

    var actualSteps by remember { mutableStateOf(0) }

    val data by model.getInfo().observeAsState()

    if (data != null) {
        targetSteps = data!!.targetSteps.toString()
        targetCals = data!!.targetCals.toString()
        targetHours = data!!.targetHours.toString()
        totalSteps = data!!.totalSteps.toInt()
        beginSteps = data!!.stepBeginOfDay.toInt()
        weight = data!!.weight
        height = data!!.height
    }

    totalCals =
        (distance / 0.75 * (0.57 * weight * 2.2) / (160934.4 / (height * 0.415))).toInt()

    totalHours = ((list.sum()) / 60).toInt()

    actualSteps = totalSteps - beginSteps

    val textArray = listOf(
        Triple(R.drawable.step, Pair(actualSteps, targetSteps), R.string.steps),
        Triple(R.drawable.cal, Pair(totalCals, targetCals), R.string.activecal),
        Triple(R.drawable.clock, Pair(totalHours, targetHours), R.string.activeTime),
    )

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
            navigationIcon = if (navController.previousBackStackEntry != null) {
                {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            } else {
                null
            })
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
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
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp, bottom = 20.dp),
                        fontFamily = semibold, fontSize = 16.sp
                    )
                    if (totalSteps != 0) {
                        val points = mutableListOf(
                            BarEntry(
                                1.toFloat(),
                                actualSteps.toFloat() / targetSteps.toFloat()
                            ),
                            BarEntry(
                                2.toFloat(),
                                totalCals.toFloat() / 2.toFloat() / targetCals.toFloat()
                            ),
                            BarEntry(
                                3.toFloat(),
                                totalHours.toFloat() / targetHours.toFloat()
                            ),
                        )
                        GraphBarChar(points = points)
                    }
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
                                Text(
                                    stringResource(id = it.third),
                                    color = smallText,
                                    fontSize = 14.sp,
                                    fontFamily = regular,
                                    modifier = Modifier.padding(bottom = 15.dp)
                                )
                                Image(
                                    painterResource(id = it.first),
                                    "",
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .size(20.dp)
                                )
                                Text(
                                    "${it.second.first} / ${it.second.second}",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = semibold
                                )
                            }
                        }
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
                            "${distance.toInt()} m", color = Color.White,
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
                            "$totalCals Cal", color = Color.White,
                            fontFamily = semibold, fontSize = 14.sp
                        )
                    }
                }
            }
            LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
                items(listExercise) { item ->
                    Card(
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                            .fillMaxWidth(),
                        backgroundColor = card,
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    stringResource(id = R.string.exercise), color = Color.White,
                                    fontFamily = regular, fontSize = 16.sp
                                )
                                Text(
                                    item.timeStart.slice(11..15), color = smallText,
                                    fontFamily = regular, fontSize = 14.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp, bottom = 20.dp)
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
                                        item.duration, color = Color.White,
                                        fontFamily = semibold, fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                                Row() {
                                    Image(
                                        painterResource(id = R.drawable.distance),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        item.distance.toString() + " m", color = Color.White,
                                        fontFamily = semibold, fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}