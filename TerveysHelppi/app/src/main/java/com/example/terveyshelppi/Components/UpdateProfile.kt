package com.example.terveyshelppi.Components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.ResultViewModel
import com.example.terveyshelppi.ui.theme.*
import java.util.*

@Composable
fun UpdateProfile(model: ResultViewModel, navController: NavController) {
    val TAG = "terveyshelppi"
    val mContext = LocalContext.current

    val data by model.getInfo().observeAsState()
    if (data != null) {

        var name by remember { mutableStateOf(data!!.name) }
        var weight by remember { mutableStateOf(data!!.weight.toString()) }
        var height by remember { mutableStateOf(data!!.height.toString()) }
        var steps by remember { mutableStateOf(data!!.targetSteps.toString()) }
        var cal by remember { mutableStateOf(data!!.targetCals.toString()) }
        var hours by remember { mutableStateOf(data!!.targetHours.toString()) }

        val state by model.state.observeAsState(true)

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            background
                        )
                    )
                )
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly) {
                //basic info
                Text(
                    stringResource(id = R.string.update),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = semibold,
                    modifier = Modifier.padding(top = 30.dp, start = 30.dp)
                )
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(id = R.string.name)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )
                TextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text(stringResource(id = R.string.weight)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )
                TextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text(stringResource(id = R.string.height)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )

                //set up goals
                Text(
                    stringResource(id = R.string.change),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = semibold,
                    modifier = Modifier.padding(top = 30.dp, start = 30.dp)
                )

                TextField(
                    value = steps,
                    onValueChange = { steps = it },
                    label = { Text(stringResource(id = R.string.step)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )
                Text(
                    "recommended 6000 steps/day",
                    color = smallText,
                    fontSize = 14.sp,
                    fontFamily = regular,
                    modifier = Modifier.padding(start = 30.dp)
                )
                TextField(
                    value = cal,
                    onValueChange = { cal = it },
                    label = { Text(stringResource(id = R.string.cal)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )
                Text(
                    "recommended 1000 Cals/day",
                    color = smallText,
                    fontSize = 14.sp,
                    fontFamily = regular,
                    modifier = Modifier.padding(start = 30.dp)
                )

                TextField(
                    value = hours.toString(),
                    onValueChange = { hours = it },
                    label = { Text(stringResource(id = R.string.hour)) },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 30.dp, end = 30.dp)
                        .fillMaxWidth()
                        .border(
                            BorderStroke(
                                2.dp, brush = Brush.verticalGradient(
                                    colors = listOf(
                                        main1, main2
                                    )
                                )
                            ), shape = RoundedCornerShape(10)
                        )
                        .background(Color.White, shape = RoundedCornerShape(10))
                )
                Text(
                    "recommended at least 30 mins/day",
                    color = smallText,
                    fontSize = 14.sp,
                    fontFamily = regular,
                    modifier = Modifier.padding(start = 30.dp)
                )
                Image(
                    painterResource(id = R.drawable.ok),
                    "",
                    modifier = Modifier
                        .padding(top = 20.dp, end = 30.dp, bottom = 20.dp)
                        .size(50.dp)
                        .clickable(onClick = {
                            if (weight.toIntOrNull() == null) {
                                Toast
                                    .makeText(
                                        mContext,
                                        "Please enter your weight in number!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else if (height.toIntOrNull() == null) {
                                Toast
                                    .makeText(
                                        mContext,
                                        "Please enter your height in number!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else if (steps.toIntOrNull() == null || cal.toIntOrNull() == null || hours.toIntOrNull() == null) {
                                Toast
                                    .makeText(
                                        mContext,
                                        "Please enter your target in number!",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else {
                                val day = Calendar
                                    .getInstance()
                                    .get(Calendar.DAY_OF_YEAR)
                                val user = UserData(
                                    name,
                                    weight.toInt(),
                                    height.toInt(),
                                    steps.toInt(),
                                    cal.toInt(),
                                    hours.toInt(),
                                    data!!.heartRate,
                                    data!!.totalDistance,
                                    data!!.totalCalories,
                                    data!!.totalSteps,
                                    data!!.totalHours,
                                    data!!.avatar,
                                    data!!.stepBeginOfDay,
                                    day
                                )
                                Log.d(TAG, "UpdateProfile: user info $user")
                                model.updateInfo(user)
                                if (state) navController.navigateUp()
                            }
                        })
                        .align(Alignment.End)

                )
            }
        }
    }
}
