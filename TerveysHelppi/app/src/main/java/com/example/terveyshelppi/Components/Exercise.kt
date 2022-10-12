package com.example.terveyshelppi.Components

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.StopWatch
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.Service.getAddress
import com.example.terveyshelppi.Service.showPoint
import com.example.terveyshelppi.ui.theme.*
import org.osmdroid.util.GeoPoint
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Exercise(navController: NavController, model: ResultViewModel) {
    val context = LocalContext.current

    val distance: Double by model.distanceRecording.observeAsState(0.0)
    val speed: Double by model.speed.observeAsState(0.0)
    val firstAltitude: Double by model.firstAltitude.observeAsState(0.0)
    val secondAltitude: Double by model.secondAltitude.observeAsState(0.0)
    val heartRate: Int by model.mBPM.observeAsState(0)

    val stopWatch = remember { StopWatch() }

    val long by model.long.observeAsState()
    val lat by model.lat.observeAsState()
    var bool by remember { mutableStateOf(1) }

    val data by model.getInfo().observeAsState()
    var height by remember { mutableStateOf(0)}
    var weight by remember { mutableStateOf(0)}
    var calories by remember { mutableStateOf(0) }

    if(data != null) {
        height = data!!.height
        weight = data!!.weight
    }

    calories = (distance/0.75 * (0.57 * weight * 2.2) / (160934.4 / (height * 0.415))).toInt()

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
                Image(
                    painterResource(id = R.drawable.music),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .clickable {
                            val intent = Intent("android.intent.action.MUSIC_PLAYER");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(context, intent, Bundle());
                        }
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            if (lat != 0.0 && long != 0.0)
                lat?.let { long?.let { it1 -> GeoPoint(it, it1) } }?.let {
                    long?.let { it1 -> getAddress(context = context, lat!!, it1) }?.let { it2 ->
                        showPoint(
                            geoPoint = it,
                            address = it2
                        )
                    }
                }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                val textArray = listOf(
                    Triple("Distance", round(distance), "m"),
                    Triple("Duration", stopWatch.formattedTime, ""),
                    Triple("Speed", round(speed), "km/h"),
                    Triple("Elevation", round(secondAltitude - firstAltitude), "m"),
                    Triple("Calories", calories, "Cal"),
                    Triple("Heart rate", heartRate, "BPM"),
                )
                Log.d("terveyshelppi", "Exercise: speed is $speed and ${speed * 3.6}")

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
                                    modifier = Modifier.padding(top = 20.dp)
                                )
                                Row(
                                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
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
            if (bool == 1) {
                Button(
                    onClick = {
                        stopWatch.start()
                        model.recording.postValue(true)
                        bool = 2
                    },
                    modifier = Modifier.padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                ) {
                    Text(
                        stringResource(id = R.string.start),
                        color = Color.White,
                        fontFamily = semibold,
                        fontSize = 18.sp
                    )
                }
            } else if (bool == 2) {
                Button(
                    onClick = {
                        stopWatch.pause()
                        model.recording.postValue(false)
                        bool = 3
                    },
                    modifier = Modifier.padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                ) {
                    Text(
                        stringResource(id = R.string.pause),
                        color = Color.White,
                        fontFamily = semibold,
                        fontSize = 18.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            stopWatch.start()
                            model.recording.postValue(true)
                            bool = 2
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                    ) {
                        Text(
                            stringResource(id = R.string.resume),
                            color = Color.White,
                            fontFamily = semibold,
                            fontSize = 18.sp
                        )
                    }
                    Button(
                        onClick = {
                            model.recording.postValue(false)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF7ED67D)),
                        modifier = Modifier.padding(start = 20.dp),
                    ) {
                        Text(
                            stringResource(id = R.string.finish),
                            color = Color.White,
                            fontFamily = semibold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

