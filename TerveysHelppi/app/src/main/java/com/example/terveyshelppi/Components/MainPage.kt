package com.example.terveyshelppi.Components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.*

@Composable
fun MainPage(model: ResultViewModel) {
    val heartRate by model.mBPM.observeAsState()
    val highHeartRate by model.highmBPM.observeAsState()
    val lowHeartRate by model.lowmBPM.observeAsState()

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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row() {
                Text(
                    stringResource(id = R.string.gm),
                    color = Color.White,
                    modifier = Modifier.padding(top = 30.dp, start = 20.dp),
                    fontFamily = regular,
                    fontSize = 20.sp
                )
                Text(
                    stringResource(id = R.string.username),
                    color = Color.White,
                    modifier = Modifier.padding(top = 30.dp, start = 5.dp),
                    fontFamily = semibold,
                    fontSize = 20.sp
                )
                Image(
                    painterResource(id = R.drawable.sun),
                    "",
                    modifier = Modifier
                        .padding(top = 35.dp, start = 10.dp)
                        .size(20.dp)
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Text(
                    stringResource(id = R.string.daily), color = Color.White,
                    modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                    fontFamily = semibold, fontSize = 14.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp, end = 20.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.step),
                        "",
                        modifier = Modifier.size(20.dp)
                    )
                    Image(
                        painterResource(id = R.drawable.cal),
                        "",
                        modifier = Modifier.size(20.dp)
                    )
                    Image(
                        painterResource(id = R.drawable.clock),
                        "",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(top = 95.dp, start = 10.dp, end = 10.dp, bottom = 20.dp)
                ) {
                    Text("100/600", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                    Text("52/250", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                    Text("15/120", color = Color.White, fontSize = 15.sp, fontFamily = semibold)
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Text(
                    stringResource(id = R.string.exercise), color = Color.White,
                    modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                    fontFamily = semibold, fontSize = 14.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
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
                            painterResource(id = R.drawable.walk),
                            "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Button(
                        onClick = {
                        },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = button),
                    ) {
                        Image(
                            painterResource(id = R.drawable.run),
                            "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Button(
                        onClick = {
                        },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = button),
                    ) {
                        Image(
                            painterResource(id = R.drawable.bike),
                            "",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                    Button(
                        onClick = {
                        },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = button),
                    ) {
                        Image(
                            painterResource(id = R.drawable.menu),
                            "",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Text(
                    stringResource(id = R.string.body), color = Color.White,
                    modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                    fontFamily = semibold, fontSize = 14.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Text(
                        "-- " + stringResource(id = R.string.kg), color = Color.White,
                        fontFamily = semibold, fontSize = 14.sp
                    )
                    Text(
                        "-- " + stringResource(id = R.string.cm), color = Color.White,
                        fontFamily = semibold, fontSize = 14.sp
                    )
                    Button(
                        onClick = {
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                        contentPadding = PaddingValues(7.dp),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.Reset),
                            color = Color.White,
                            fontFamily = regular,
                            fontSize = 12.sp,
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Text(
                    stringResource(id = R.string.heart), color = Color.White,
                    modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                    fontFamily = semibold, fontSize = 14.sp
                )
                Row(
                    modifier = Modifier
                        .padding(top = 60.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Text(
                        heartRate.toString() + stringResource(id = R.string.bpm),
                        color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (highHeartRate == 0) "Highest: --" else "Highest: $highHeartRate",
                        color = Color.White,
                        fontFamily = light,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = if (lowHeartRate == 300) "Lowest: --" else "Lowest: $lowHeartRate",
                        color = Color.White,
                        fontFamily = light,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Button(
                        onClick = {
                            model.highmBPM.postValue(0)
                            model.lowmBPM.postValue(300)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                        contentPadding = PaddingValues(7.dp),
                        modifier = Modifier
                            .padding(start = 165.dp)
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.Reset),
                            color = Color.White,
                            fontFamily = regular,
                            fontSize = 12.sp,
                        )
                    }
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                        contentPadding = PaddingValues(7.dp),
                        modifier = Modifier
                            .padding(start = 165.dp)
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.graph),
                            color = Color.White,
                            fontFamily = regular,
                            fontSize = 12.sp,
                        )
                    }

                }
            }
        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun MainPagePreview() {
//    TerveysHelppiTheme {
//        MainPage()
//    }
//}