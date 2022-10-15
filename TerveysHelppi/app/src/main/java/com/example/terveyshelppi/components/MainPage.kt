package com.example.terveyshelppi.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
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
import androidx.navigation.NavController
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TextModifiedWithId
import com.example.terveyshelppi.libraryComponent.TextModifiedWithPaddingStart
import com.example.terveyshelppi.libraryComponent.TextModifiedWithString
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.ui.theme.button
import com.example.terveyshelppi.ui.theme.card
import com.example.terveyshelppi.ui.theme.regular
import com.example.terveyshelppi.ui.theme.semibold
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainPage(navController: NavController, model: ResultViewModel) {
    val tag = "terveyshelppi"
    val context = LocalContext.current

    //get time of day
    val c = Calendar.getInstance()
    val timeOfDay = c.get(Calendar.HOUR_OF_DAY)
    val today = c.time.toString().slice(0..9)
    // set text of hello user
    var text by remember { mutableStateOf("") }
    text = when (timeOfDay) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..23 -> "Good Night"
        else -> "Hello"
    }

    //exercise data
    val exerciseData by model.getAllExercises().observeAsState()
    val list = mutableListOf(0L)
    exerciseData?.forEach {
        if (it.timeStart.slice(0..9) == today) list.add(it.activeTime)
    }
    Log.d(tag, "MainPage: list of time workout $list")

    val distance: Double by model.distance.observeAsState(0.0)
    var user by remember { mutableStateOf("") }
    var totalSteps by remember { mutableStateOf(0) }
    var totalCalories by remember { mutableStateOf(0) }
    var totalHours by remember { mutableStateOf(0) }
    var targetSteps by remember { mutableStateOf(0) }
    var targetHours by remember { mutableStateOf(0) }
    var targetCals by remember { mutableStateOf(0) }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var beginStep by remember { mutableStateOf(0) }

    val data by model.getInfo().observeAsState()
    val heartRate by model.mBPM.observeAsState()
    val highHeartRate by model.highmBPM.observeAsState()
    val lowHeartRate by model.lowmBPM.observeAsState()
    val temp by model.tempValue.observeAsState()
    val graph by model.graph.observeAsState()

    Log.d(tag, "MainPage: temperature = $temp")

    // set data from Room
    if (data != null) {
        user = data?.name.toString()
        totalSteps = data!!.totalSteps.toInt()
        weight = data!!.weight
        height = data!!.height
        targetSteps = data!!.targetSteps
        targetCals = data!!.targetCals
        targetHours = data!!.targetHours
        beginStep = data!!.stepBeginOfDay.toInt()
        Log.d(tag, "MainPage: today is ${Calendar.getInstance().time}")
        Log.d(
            tag,
            "MainPage: totalStep is $totalSteps and stepBeginOfDay is ${data!!.stepBeginOfDay}"
        )
    }
    // calculator data
    totalCalories =
        (distance / 0.75 * (0.57 * weight * 2.2) / (160934.4 / (height * 0.415))).toInt()
    totalHours = list.sum().toInt()
    Log.d(tag, "MainPage: userinfo $data")

    // set data for displaying
    val textArray = listOf(
        Triple(R.drawable.step, totalSteps - beginStep, targetSteps),
        Triple(R.drawable.cal, totalCalories, targetCals),
        Triple(R.drawable.clock, totalHours / 60, targetHours),
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
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Row(
                modifier = Modifier.padding(top = 30.dp, start = 20.dp, bottom = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    TextModifiedWithString(string = text, font = regular, size = 20)
                    TextModifiedWithPaddingStart(
                        string = if (user != "") user else stringResource(id = R.string.username),
                        size = 20
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
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // display result activity of day
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
                    Column(
                        modifier = Modifier.padding(
                            top = 15.dp,
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 20.dp
                        )
                    ) {
                        TextModifiedWithId(id = R.string.daily, font = semibold, size = 16)
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
                // card of activity
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
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                        // clear points of location before
                                        model.points.postValue(mutableListOf())
                                        navController.navigate("exercise")
                                    },
                                    modifier = Modifier.size(45.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF1E0F32
                                        )
                                    ),
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
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .clickable {
                                            // clear points of location before
                                            model.points.postValue(mutableListOf())
                                            navController.navigate("exercise")
                                        }
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                        if (exerciseData != null) {
                                            if (exerciseData!!.isNotEmpty()) {
                                                Log.d(tag,
                                                    "MainPage: exercise 0 is ${exerciseData!![0]}")
                                                navController.navigate(
                                                    "history")
                                            } else {
                                                Toast
                                                    .makeText(context,
                                                        "You haven't exercised today!",
                                                        Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        } else {
                                            Toast
                                                .makeText(context,
                                                    "You haven't exercised today!",
                                                    Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    },
                                    modifier = Modifier.size(45.dp),
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color(
                                            0xFF1E0F32
                                        )
                                    ),
                                ) {
                                    Image(
                                        painterResource(id = R.drawable.menu),
                                        "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    stringResource(id = R.string.history), color = Color.White,
                                    fontFamily = semibold, fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .clickable {
                                            if (exerciseData != null) {
                                                if (exerciseData!!.isNotEmpty()) {
                                                    Log.d(tag,
                                                        "MainPage: exercise 0 is ${exerciseData!![0]}")
                                                    navController.navigate(
                                                        "history")
                                                } else {
                                                    Toast
                                                        .makeText(context,
                                                            "You haven't exercised today!",
                                                            Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            } else {
                                                Toast
                                                    .makeText(context,
                                                        "You haven't exercised today!",
                                                        Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
                // card of user info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                        .clickable {
                                   navController.navigate("update")
                        },
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
                                TextModifiedWithPaddingStart(
                                    string = weight.toString() + " " + stringResource(id = R.string.kg),
                                    paddingStart = 10
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
                                TextModifiedWithPaddingStart(
                                    string = height.toString() + " " + stringResource(id = R.string.cm),
                                    size = 14,
                                    paddingStart = 10
                                )
                            }
                        }
                    }
                }
                // card of heart rate info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .clickable {
                            // navigate to graph
                            if (graph != null) {
                                if (graph!!.size > 1) {
                                    navController.navigate("graph-heartRate")
                                } else Toast
                                    .makeText(context,
                                        "No data for heart rate graph",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            } else Toast
                                .makeText(context,
                                    "No data for heart rate graph",
                                    Toast.LENGTH_SHORT)
                                .show()
                        },
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 25.dp, bottom = 20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextModifiedWithId(id = R.string.heart, font = semibold, size = 16)
                            Button(
                                onClick = {
                                    model.highmBPM.postValue(0)
                                    model.lowmBPM.postValue(300)
                                },
                                modifier = Modifier
                                    .size(40.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(
                                        0xFF1E0F32
                                    )
                                ),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.reload),
                                    "",
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                            TextModifiedWithPaddingStart(
                                string = heartRate.toString() + stringResource(id = R.string.bpm),
                                size = 16,
                                paddingStart = 10
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
                        }

                    }
                }
            }
        }
    }
}
