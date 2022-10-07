package com.example.terveyshelppi.Components

import androidx.compose.foundation.BorderStroke
import com.example.terveyshelppi.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.ui.theme.*

@Composable
fun MainPage() {
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
            Row(modifier = Modifier.padding(top = 30.dp, start = 20.dp, bottom = 15.dp)) {
                Text(
                    stringResource(id = R.string.gm),
                    color = Color.White,
                    fontFamily = regular,
                    fontSize = 20.sp
                )
                Text(
                    stringResource(id = R.string.username),
                    color = Color.White,
                    modifier = Modifier.padding(start = 5.dp),
                    fontFamily = semibold,
                    fontSize = 20.sp
                )
                Image(
                    painterResource(id = R.drawable.sun),
                    "",
                    modifier = Modifier
                        .padding(top = 5.dp, start = 10.dp)
                        .size(20.dp)
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 10.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.daily), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                                .padding(top = 20.dp, bottom = 10.dp)
                                .fillMaxWidth()
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
                                .padding(bottom = 20.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "100/600",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                            Text(
                                "52/250",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                            Text(
                                "15/120",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontFamily = semibold
                            )
                        }
                    }
                }
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
                                .fillMaxWidth()
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
                                    painterResource(id = R.drawable.walk),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.run),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bike),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                modifier = Modifier.size(45.dp),
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = button),
                            ) {
                                Image(
                                    painterResource(id = R.drawable.menu),
                                    "",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(top = 15.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.body), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp, start = 10.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
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
                            ) {
                                Text(
                                    stringResource(id = R.string.record),
                                    color = Color.White,
                                    fontFamily = regular,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }

                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    backgroundColor = card,
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding( start = 10.dp, top = 15.dp, end = 10.dp)) {
                        Text(
                            stringResource(id = R.string.heart), color = Color.White,
                            fontFamily = semibold, fontSize = 16.sp
                        )
                        Row(
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row() {
                                Text(
                                    "95 " + stringResource(id = R.string.bpm), color = Color.White,
                                    fontFamily = semibold, fontSize = 14.sp
                                )
                                Text(
                                    "15:30",
                                    color = Color.White,
                                    fontFamily = light,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            Button(
                                onClick = {
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = button2),
                            ) {
                                Text(
                                    stringResource(id = R.string.record),
                                    color = Color.White,
                                    fontFamily = regular,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    TerveysHelppiTheme {
        MainPage()
    }
}