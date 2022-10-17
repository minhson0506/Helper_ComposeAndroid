package com.example.terveyshelppi.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TextModifiedWithId
import com.example.terveyshelppi.libraryComponent.TextModifiedWithPaddingStart
import com.example.terveyshelppi.libraryComponent.TextModifiedWithString
import com.example.terveyshelppi.libraryComponent.TopAppBarWithBackButton
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.service.roomDB.ExerciseData
import com.example.terveyshelppi.ui.theme.card
import com.example.terveyshelppi.ui.theme.regular
import com.example.terveyshelppi.ui.theme.semibold
import com.example.terveyshelppi.ui.theme.smallText
import com.github.mikephil.charting.data.BarEntry
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyActivity(model: ResultViewModel, navController: NavController) {
    val tag = "terveyshelppi"

    // get date of system
    val time = Calendar.getInstance().time.toString().slice(0..10)

    // get exercise data
    val exerciseData by model.getAllExercises().observeAsState()

    // for display today's exercises only
    val list = mutableListOf(0L)
    val listExercise = mutableListOf<ExerciseData>()
    exerciseData?.forEach {
        if (it.timeStart.slice(0..10) == time) {
            list.add(it.activeTime)
            listExercise.add(it)
        }
    }

    val distance: Double by model.distance.observeAsState(0.0)
    Log.d(tag, "DailyActivity: model distance in Daily $distance")
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

    // get user data from room
    val userData by model.getInfo().observeAsState()
    if (userData != null) {
        targetSteps = userData!!.targetSteps.toString()
        targetCals = userData!!.targetCals.toString()
        targetHours = userData!!.targetHours.toString()
        totalSteps = userData!!.totalSteps.toInt()
        beginSteps = userData!!.stepBeginOfDay.toInt()
        weight = userData!!.weight
        height = userData!!.height
    }

    // calculator calories, active hours, steps in that day
    totalCals =
        (distance / 0.75 * (0.57 * weight * 2.2) / (160934.4 / (height * 0.415))).toInt()
    totalHours = list.sum().toInt()
    actualSteps = totalSteps - beginSteps

    // data for display in grid view
    val textArray = listOf(
        Triple(R.drawable.step, Pair(actualSteps, targetSteps), R.string.steps),
        Triple(R.drawable.cal, Pair(totalCals, targetCals), R.string.activecal),
        Triple(R.drawable.clock, Pair(totalHours / 60, targetHours), R.string.activeTime),
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
        TopAppBarWithBackButton(navController = navController, id = R.string.daily)
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
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
                        stringResource(id = R.string.today),
                        color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp),
                        fontFamily = semibold,
                        fontSize = 16.sp
                    )

                    // only display graph when user started to exercise
                    if (totalSteps != 0) {
                        Text(
                            stringResource(id = R.string.barChart),
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .align(CenterHorizontally),
                            textAlign = TextAlign.Center,
                            fontFamily = semibold,
                            fontSize = 12.sp
                        )

                        // get data for graph
                        val points = mutableListOf(
                            BarEntry(
                                1.toFloat(),
                                (actualSteps * 100).toFloat() / targetSteps.toFloat()
                            ),
                            BarEntry(
                                2.toFloat(),
                                (totalCals * 100).toFloat() / 2.toFloat() / targetCals.toFloat()
                            ),
                            BarEntry(
                                3.toFloat(),
                                totalHours.toFloat() / 60 * 100 / targetHours.toFloat()
                            ),
                        )
                        // display graph
                        GraphBarChart(points = points)
                    }
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(3),
                        modifier = Modifier.padding(
                            start = 10.dp,
                            end = 10.dp
                        )
                    ) {
                        items(textArray) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = CenterHorizontally,
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
                        TextModifiedWithId(id = R.string.distance)
                        TextModifiedWithString(string = "${distance.toInt()} m")
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextModifiedWithId(id = R.string.total_cal)
                        TextModifiedWithString(string = "$totalCals Cal")
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
                                TextModifiedWithId(id = R.string.exercise)
                                Text(
                                    item.timeStart.slice(11..15),
                                    color = smallText,
                                    fontFamily = regular,
                                    fontSize = 14.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 15.dp, bottom = 20.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Image(
                                        painterResource(id = R.drawable.timer),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    TextModifiedWithPaddingStart(string = item.duration)
                                }
                                Row {
                                    Image(
                                        painterResource(id = R.drawable.distance),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                    TextModifiedWithPaddingStart(string = item.distance.toString() + " m")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}