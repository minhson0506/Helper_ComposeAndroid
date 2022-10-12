package com.example.terveyshelppi.Components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
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
import com.example.terveyshelppi.ui.theme.*
import com.github.mikephil.charting.data.BarEntry

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyActivity(model: ResultViewModel, navController: NavController) {
    val TAG = "terveyshelppi"

    var totalSteps by remember { mutableStateOf(0) }
    var totalCals by remember { mutableStateOf("") }
    var totalHours by remember { mutableStateOf("") }
    var targetSteps by remember { mutableStateOf("") }
    var targetCals by remember { mutableStateOf("") }
    var targetHours by remember { mutableStateOf("") }

    val data by model.getInfo().observeAsState()

    if (data != null) {
        targetSteps = data!!.targetSteps.toString()
        targetCals = data!!.targetCals.toString()
        targetHours = data!!.targetHours.toString()
        totalSteps = data!!.totalSteps.toInt()
        totalCals = data!!.totalCalories.toString()
        totalHours = data!!.totalHours.toString()
    }

    val textArray = listOf(
        Triple(R.drawable.step, Pair(totalSteps, targetSteps), R.string.steps),
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
            },
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
                                totalSteps.toFloat() / targetSteps.toFloat()
                            ),
                            BarEntry(
                                2.toFloat(),
                                totalSteps.toFloat() / 2.toFloat() / targetSteps.toFloat()
                            )
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
                                    modifier = Modifier.padding(bottom = 10.dp).size(20.dp)
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
                modifier = Modifier.padding(all = 20.dp)
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
                            "09:30", color = smallText,
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