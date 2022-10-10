package com.example.terveyshelppi.Components

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.terveyshelppi.Service.Notification.Notification
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePage() {
    val mContext = LocalContext.current
    val notificationId = 1

    var mDisplayMenu by remember { mutableStateOf(false) }

    fun setNotification() {
        //this intent link to Notification class
        val intent = Intent(mContext, Notification::class.java)
        intent.putExtra("notification", notificationId)

        val alarmIntent =
            PendingIntent.getBroadcast(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


        val alarm = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val hour = Calendar.HOUR
        val minute = Calendar.MINUTE + 1

        //call Calendar singleton
        val startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = hour
        startTime[Calendar.MINUTE] = minute
        startTime[Calendar.SECOND] = 0
        val alarmStartTime = startTime.timeInMillis

        alarm.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmStartTime,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

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
                    stringResource(id = R.string.profile),
                    color = Color.White,
                    fontFamily = regular
                )
            }, backgroundColor = Color.Black,
            actions = {
                // Creating Icon button for dropdown menu
                Image(
                    painterResource(id = R.drawable.options),
                    "",
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(25.dp)
                        .clickable { mDisplayMenu = !mDisplayMenu }
                )

                // Creating a dropdown menu
                DropdownMenu(
                    expanded = mDisplayMenu,
                    onDismissRequest = { mDisplayMenu = false }
                ) {
                    DropdownMenuItem(onClick = {
                        Toast.makeText(
                            mContext,
                            "Edit profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Text(text = "Edit profile")
                    }
                    DropdownMenuItem(onClick = {
                        Toast.makeText(mContext, "Update goals", Toast.LENGTH_SHORT).show()
                    }) {
                        Text(text = "Update goals")
                    }
                    DropdownMenuItem(onClick = {
                        Toast.makeText(mContext, "Notifcation is set!", Toast.LENGTH_SHORT).show()
                        setNotification()
                    }) {
                        Text(
                            text = "Set notification"
                        )
                    }
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(top = 30.dp, bottom = 20.dp)
                .fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Creating a Top bar
            Image(
                painterResource(id = R.drawable.dog),
                contentDescription = "dog",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .align(CenterHorizontally)
                    .size(100.dp)
                    .clip(CircleShape)

            )
            Text(
                stringResource(id = R.string.username),
                color = Color.White,
                fontFamily = semibold,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 10.dp, bottom = 10.dp),
                fontSize = 18.sp
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp),
                backgroundColor = card,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(bottom = 20.dp)) {
                    Text(
                        stringResource(id = R.string.weekly), color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp),
                        fontFamily = semibold, fontSize = 16.sp
                    )
                    Text(
                        stringResource(id = R.string.date), color = smallText,
                        modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                        fontFamily = light, fontSize = 12.sp
                    )
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(id = R.string.average), color = Color.White,
                            modifier = Modifier.padding(start = 10.dp),
                            fontFamily = regular, fontSize = 14.sp
                        )
                        Row() {
                            Text(
                                stringResource(id = R.string.time), color = Color.White,
                                fontFamily = semibold, fontSize = 14.sp
                            )
                            Text(
                                stringResource(id = R.string.min), color = Color.White,
                                modifier = Modifier.padding(start = 5.dp, end = 10.dp),
                                fontFamily = light, fontSize = 14.sp
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
                val textArray = listOf(
                    Triple(R.drawable.step, 2000, Pair("steps", "Most steps")),
                    Triple(R.drawable.floor, 3, Pair("floors", "Most floors")),
                    Triple(R.drawable.clock, 50, Pair("minutes", "Duration")),
                    Triple(R.drawable.cal, 100, Pair("Cals", "Calories burnt")),
                    Triple(R.drawable.distance, 2, Pair("km", "Distance")),
                    Triple(R.drawable.elevation, 20, Pair("m", "Elevation")),
                    Triple(R.drawable.speed, 20, Pair("km/h", "Speed"))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.pb), color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp, bottom = 20.dp),
                        fontFamily = semibold, fontSize = 16.sp
                    )
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(3)
                    ) {
                        items(textArray) {
                            Card(
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 10.dp
                                ),
                                backgroundColor = button
                            ) {
                                Column(horizontalAlignment = CenterHorizontally) {
                                    Image(
                                        painterResource(id = it.first),
                                        "",
                                        modifier = Modifier
                                            .padding(top = 10.dp)
                                            .size(20.dp)
                                    )
                                    Text(
                                        text = it.second.toString(),
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontFamily = semibold,
                                        modifier = Modifier.padding(top = 10.dp)
                                    )
                                    Text(
                                        text = it.third.first,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontFamily = light,
                                        modifier = Modifier.padding(top = 5.dp)
                                    )
                                    Text(
                                        text = it.third.second,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontFamily = regular,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TerveysHelppiTheme {
        ProfilePage()
    }
}