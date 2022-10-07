package com.example.terveyshelppi.Components

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Exercise(navController: NavController) {
    var distance by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }
    var speed by remember { mutableStateOf(0) }
    var elevation by remember { mutableStateOf(0) }
    var heartRate by remember { mutableStateOf(0) }
    var calories by remember { mutableStateOf(0) }

    var bool by remember { mutableStateOf(1) }

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
                    stringResource(id = R.string.walking),
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
                // Creating Icon button for dropdown menu
                Image(
                    painterResource(id = R.drawable.map),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .clickable { }
                )
                Image(
                    painterResource(id = R.drawable.music),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .clickable { }
                )
                Image(
                    painterResource(id = R.drawable.options),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(20.dp)
                        .clickable { }
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 30.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                val textArray = listOf(
                    Triple("Distance", distance, "km"),
                    Triple("Duration", duration, ""),
                    Triple("Speed", speed, "km/h"),
                    Triple("Elevation", elevation, "m"),
                    Triple("Calories", calories, "Cals"),
                    Triple("Heart rate", heartRate, "BPM"),
                )

                LazyVerticalGrid(
                    cells = GridCells.Fixed(2)
                ) {
                    items(textArray) {
                        Card(
                            modifier = Modifier.padding(
                                all = 10.dp
                            ),
                            backgroundColor = button
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = it.first,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontFamily = semibold,
                                    modifier = Modifier.padding(top = 30.dp)
                                )
                                Row(
                                    modifier = Modifier.padding(top = 10.dp, bottom = 30.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = it.second.toString(),
                                        color = Color.White,
                                        fontSize = 20.sp,
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
                        bool = 2
                    },
                    modifier = Modifier.padding(top = 20.dp),
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
                        bool = 3
                    },
                    modifier = Modifier.padding(top = 20.dp),
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
                    modifier = Modifier.padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            bool = 1
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
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = button2),
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

