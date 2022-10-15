package com.example.terveyshelppi.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TextModifiedWithPaddingStart
import com.example.terveyshelppi.libraryComponent.TextModifiedWithString
import com.example.terveyshelppi.libraryComponent.TopAppBarWithBackButton
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.service.mapGG
import com.example.terveyshelppi.service.roomDB.ExerciseData
import com.example.terveyshelppi.ui.theme.*
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseResult(navController: NavController, model: ResultViewModel) {
    //fetch last exercise
    val data by model.getAllExercises().observeAsState()
    var lastItem by remember { mutableStateOf<ExerciseData?>(null) }
    if (data != null) {
        lastItem = data!![data!!.size - 1]
    }

    val points by model.points.observeAsState()

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
        TopAppBarWithBackButton(navController = navController, id = R.string.exercise)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                // date for display
                val textArray = listOf(
                    Triple("Total Distance", lastItem?.distance.toString(), "m"),
                    Triple("Duration", lastItem?.duration, ""),
                    Triple("Avg. Speed",
                        lastItem?.let { round(it.averageSpeed * 3.6).toString() },
                        "km/h"),
                    Triple("Elevation", lastItem?.elevation.toString(), "m"),
                    Triple("Calories Burnt", lastItem?.calories.toString(), "Cal"),
                    Triple("Avg. Heart Rate",
                        lastItem?.let { round(it.heartRate).toString() },
                        "BPM"),
                )
                Column {
                    Text(
                        stringResource(id = R.string.result),
                        color = Color.White,
                        fontFamily = semibold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 15.dp, start = 15.dp, bottom = 5.dp)
                    )
                    lastItem?.let {
                        Text(
                            it.timeStart.slice(0..18),
                            color = Color.White,
                            fontFamily = regular,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)
                        )
                    }
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(2)
                    ) {
                        items(textArray) {
                            Card(
                                modifier = Modifier.padding(
                                    all = 5.dp
                                ),
                                backgroundColor = button
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = it.first,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = semibold,
                                        modifier = Modifier.padding(top = 15.dp)
                                    )
                                    Row(
                                        modifier = Modifier.padding(top = 10.dp, bottom = 15.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextModifiedWithString(
                                            string = it.second.toString(),
                                            size = 18)
                                        TextModifiedWithPaddingStart(
                                            string = it.third,
                                            size = 14,
                                            font = light)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // display route of map using Google Map
            points?.let { mapGG(points = it) }
        }
    }
}