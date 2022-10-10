package com.example.terveyshelppi.Components

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.*

@Composable
fun InfoLanding(navController: NavController, application: Application) {
    val TAG = "terveyshelppi"
    val mContext = LocalContext.current

    var name by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var cal by remember { mutableStateOf("") }
    var hours by remember { mutableStateOf("") }

    val viewModel = ResultViewModel(application)

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
        Column(modifier = Modifier.fillMaxSize()) {
            //basic info
            Text(
                stringResource(id = R.string.ready),
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
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
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
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
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
                    .padding(top = 20.dp, start = 30.dp, end = 30.dp)
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
                stringResource(id = R.string.goal),
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = semibold,
                modifier = Modifier.padding(top = 30.dp, start = 30.dp)
            )

            TextField(
                value = steps.toString(),
                onValueChange = { steps = it },
                label = { Text(stringResource(id = R.string.step)) },
                modifier = Modifier
                    .padding(top = 15.dp, start = 30.dp, end = 30.dp)
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
                "recommended 6,000 steps/day",
                color = smallText,
                fontSize = 14.sp,
                fontFamily = regular,
                modifier = Modifier.padding(top = 5.dp, start = 30.dp)
            )
            TextField(
                value = cal.toString(),
                onValueChange = { cal = it },
                label = { Text(stringResource(id = R.string.cal)) },
                modifier = Modifier
                    .padding(top = 15.dp, start = 30.dp, end = 30.dp)
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
                modifier = Modifier.padding(top = 5.dp, start = 30.dp)
            )
            TextField(
                value = hours.toString(),
                onValueChange = { hours = it },
                label = { Text(stringResource(id = R.string.hour)) },
                modifier = Modifier
                    .padding(top = 15.dp, start = 30.dp, end = 30.dp)
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
                modifier = Modifier.padding(top = 5.dp, start = 30.dp)
            )
            Image(
                painterResource(id = R.drawable.next),
                "",
                modifier = Modifier
                    .padding(top = 50.dp, end = 30.dp)
                    .size(50.dp)
                    .clickable(onClick = {
                        if (name == "") {
                            Toast
                                .makeText(mContext, "Please enter your name!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        if (weight == "") {
                            Toast
                                .makeText(
                                    mContext,
                                    "Please enter your weight!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        if (height == "") {
                            Toast
                                .makeText(
                                    mContext,
                                    "Please enter your height!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                        if (hours == "") hours = "1"
                        if (steps == "") steps = "6000"
                        if (cal == "") cal = "1000"
                        if (name != "" && weight != "" && height != "") {
                            val user = UserData(
                                name,
                                weight.toInt(),
                                height.toInt(),
                                steps.toInt(),
                                cal.toInt(),
                                hours.toInt(),
                                0,
                                0,
                                0,
                                0.0,
                                0
                            )
                            Log.d(TAG, "InfoLanding: user info $user")
                            viewModel.insertUser(user)
                            navController.navigate("main")
                        }
                    })
                    .align(Alignment.End)

            )
        }
    }
}
