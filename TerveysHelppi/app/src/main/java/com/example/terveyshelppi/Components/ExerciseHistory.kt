package com.example.terveyshelppi.Components

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.Service.RoomDB.ExerciseData
import com.example.terveyshelppi.ui.theme.*
import java.util.*

@Composable
fun ExerciseHistory(navController: NavController, model: ResultViewModel) {
    val TAG = "terveyshelppi"

    val exerciseData by model.getAllExercises().observeAsState()
    val listDate = mutableSetOf<String>()

    exerciseData?.forEach {
        listDate.add(it.timeStart.slice(0..9))
    }
    Log.d(TAG, "ExerciseHistory: list date $listDate")

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
                    stringResource(id = R.string.all),
                    color = Color.White,
                    fontFamily = regular
                )
            }, backgroundColor = Color.Black,
            navigationIcon = if (navController.previousBackStackEntry != null) {
                {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            } else {
                null
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 20.dp, start = 20.dp, end = 20.dp),
        ) {
            listDate.forEach { date ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
                ) {
                    Button(
                        onClick = {
                            navController.navigate("exercise")
                        },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = button),
                    ) {
                        Image(
                            painterResource(id = R.drawable.calendar),
                            "",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    Text(
                        date, color = Color.White,
                        modifier = Modifier.padding(start = 10.dp),
                        fontFamily = regular, fontSize = 16.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .height(280.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    exerciseData?.forEach { exercise ->
                        if (exercise.timeStart.slice(0..9) == date) {
                            Card(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
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
                                            stringResource(id = R.string.exercise),
                                            color = Color.White,
                                            fontFamily = regular,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            exercise.timeStart.slice(11..15), color = smallText,
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
                                                exercise.duration, color = Color.White,
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
                                                exercise.distance.toString() + " m",
                                                color = Color.White,
                                                fontFamily = semibold,
                                                fontSize = 16.sp,
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
    }
}