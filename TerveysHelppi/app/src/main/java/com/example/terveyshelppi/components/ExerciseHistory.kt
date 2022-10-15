package com.example.terveyshelppi.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TextModifiedWithId
import com.example.terveyshelppi.libraryComponent.TextModifiedWithPaddingStart
import com.example.terveyshelppi.libraryComponent.TopAppBarWithBackButton
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.ui.theme.button
import com.example.terveyshelppi.ui.theme.card
import com.example.terveyshelppi.ui.theme.regular
import com.example.terveyshelppi.ui.theme.smallText
import kotlin.math.round

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
        Column {
            TopAppBarWithBackButton(navController = navController, id = R.string.all)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            ) {
                listDate.forEach { date ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 30.dp, bottom = 10.dp),
                    ) {
                        Button(
                            onClick = {
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
                        TextModifiedWithPaddingStart(string = date,
                            font = regular,
                            paddingStart = 10)
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // display exercise history
                        exerciseData?.forEach { exercise ->
                            if (exercise.timeStart.slice(0..9) == date) {
                                Card(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                        .fillMaxWidth(),
                                    backgroundColor = card,
                                    elevation = 4.dp
                                ) {
                                    Column(modifier = Modifier.padding(start = 15.dp,
                                        end = 15.dp)) {
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 15.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            TextModifiedWithId(id = R.string.exercise, size = 16)
                                            Text(
                                                exercise.timeStart.slice(11..15),
                                                color = smallText,
                                                fontFamily = regular,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 20.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row {
                                                Image(
                                                    painterResource(id = R.drawable.timer),
                                                    "",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                TextModifiedWithPaddingStart(
                                                    string = exercise.duration,
                                                    font = regular,
                                                    size = 15)
                                            }
                                            Row {
                                                Image(
                                                    painterResource(id = R.drawable.distance),
                                                    "",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                TextModifiedWithPaddingStart(
                                                    string = exercise.distance.toString() + " m",
                                                    font = regular,
                                                    size = 15)
                                            }
                                        }
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 15.dp, bottom = 20.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row {
                                                Image(
                                                    painterResource(id = R.drawable.speed),
                                                    "",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                TextModifiedWithPaddingStart(
                                                    string = round(exercise.averageSpeed * 3.6).toString(),
                                                    font = regular,
                                                    size = 15)
                                            }
                                            Row {
                                                Image(
                                                    painterResource(id = R.drawable.cal),
                                                    "",
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                TextModifiedWithPaddingStart(
                                                    string = exercise.calories.toString() + " Cal",
                                                    font = regular,
                                                    size = 15)
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
}