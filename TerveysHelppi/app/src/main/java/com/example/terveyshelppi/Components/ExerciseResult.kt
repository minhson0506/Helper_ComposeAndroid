package com.example.terveyshelppi.Components

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.Service.RoomDB.ExerciseData
import com.example.terveyshelppi.Service.getAddress
import com.example.terveyshelppi.Service.showPoint
import com.example.terveyshelppi.ui.theme.*
import org.osmdroid.util.GeoPoint
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseResult(navController: NavController, model: ResultViewModel) {
    val context = LocalContext.current

    //fetch last exercise
    val data by model.getAllExercises().observeAsState()
    var lastItem by remember { mutableStateOf<ExerciseData?>(null) }
    if (data != null) {
        lastItem = data!![data!!.size - 1]
    }

    val long by model.long.observeAsState()
    val lat by model.lat.observeAsState()

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
                    stringResource(id = R.string.exercise),
                    color = Color.White,
                    fontFamily = regular
                )
            }, backgroundColor = Color.Black,
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack("exercise", true)
                    navController.navigate("home") }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                val textArray = listOf(
                    Triple("Total Distance", lastItem?.distance.toString(), "m"),
                    Triple("Duration", lastItem?.duration, ""),
                    Triple("Avg. Speed", lastItem?.let { round(it.averageSpeed).toString() }, "km/h"),
                    Triple("Elevation", lastItem?.elevation.toString(), "m"),
                    Triple("Calories Burnt", lastItem?.calories.toString(), "Cal"),
                    Triple("Avg. Heart Rate", lastItem?.let { round(it.heartRate).toString() }, "BPM"),
                )
                Column() {
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
                                        Text(
                                            text = it.second.toString(),
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontFamily = semibold,
                                        )
                                        Text(
                                            text = it.third,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontFamily = light,
                                            modifier = Modifier.padding(start = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (lat != 0.0 && long != 0.0)
                lat?.let { long?.let { it1 -> GeoPoint(it, it1) } }?.let {
                    long?.let { it1 -> getAddress(context = context, lat!!, it1) }?.let { it2 ->
                        showPoint(
                            geoPoint = it,
                            address = it2
                        )
                    }
                }
        }
    }
}