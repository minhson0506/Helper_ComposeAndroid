package com.example.terveyshelppi.Components

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.terveyshelppi.Service.Notification.Notification
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.ResultViewModel
import com.google.android.libraries.maps.model.LatLng
import java.io.File
import java.util.*
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePage(navControler: NavController, model: ResultViewModel) {
    val TAG = "terveyshelppi"
    val mContext = LocalContext.current


    var mDisplayMenu by remember { mutableStateOf(false) }

    //exercise data
    val exerciseData by model.getAllExercises().observeAsState()
    val data by model.getInfo().observeAsState()

    var user by remember { mutableStateOf("") }
    var targetSteps by remember { mutableStateOf(0) }
    var targetHours by remember { mutableStateOf(0) }
    var targetCals by remember { mutableStateOf(0) }

    var maxDistance by remember { mutableStateOf(0) }
    var maxTime by remember { mutableStateOf(0L) }
    var maxCalories by remember { mutableStateOf(0) }
    var maxElevation by remember { mutableStateOf(0) }
    var maxSpeed by remember { mutableStateOf(0.0) }


    if (data != null) {
        user = data?.name.toString()
        targetSteps = data!!.targetSteps
        targetCals = data!!.targetCals
        targetHours = data!!.targetHours
    }

    if (exerciseData != null) {
        val distance = exerciseData!!.map { it.distance }
        val activeTime = exerciseData!!.map { it.activeTime }
        val calories = exerciseData!!.map { it.calories }
        val elevation = exerciseData!!.map { it.elevation }
        val speed = exerciseData!!.map { it.averageSpeed }

        maxDistance = distance.maxOrNull() ?: 0
        maxTime = activeTime.maxOrNull() ?: 0
        maxCalories = calories.maxOrNull() ?: 0
        maxElevation = elevation.maxOrNull() ?: 0
        maxSpeed = speed.maxOrNull() ?: 0.0
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
                        navControler.navigate("update")
                    }) {
                        Text(text = "Update profile")
                    }
                    DropdownMenuItem(onClick = {
                        Toast.makeText(mContext, "Notifcation is set!", Toast.LENGTH_SHORT)
                            .show()
                        setNotification(mContext)
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Camera(model)
            Text(
                user,
                color = Color.White,
                fontFamily = semibold,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 10.dp, bottom = 10.dp),
                fontSize = 18.sp
            )
            val goalArray = listOf(
                Triple(R.drawable.step, targetSteps, "steps/day"),
                Triple(R.drawable.cal, targetCals, "cals/day"),
                Triple(R.drawable.clock, targetHours, "mins/day"),
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
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(3),
                        modifier = Modifier.padding(
                            top = 20.dp,
                            start = 10.dp,
                            end = 10.dp
                        )
                    ) {
                        items(goalArray) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Image(
                                    painterResource(id = it.first),
                                    "",
                                    modifier = Modifier
                                        .padding(bottom = 15.dp)
                                        .size(20.dp)
                                )
                                Text(
                                    it.second.toString(),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontFamily = semibold,
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )

                                Text(
                                    it.third,
                                    color = smallText,
                                    fontSize = 14.sp,
                                    fontFamily = regular,
                                )
                            }
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
                    Triple(R.drawable.distance, maxDistance, Pair("m", "Distance")),
                    Triple(R.drawable.cal, maxCalories, Pair("Cal", "Most calories")),
                    Triple(R.drawable.speed, round(maxSpeed), Pair("km/h", "Highest speed")),
                    Triple(R.drawable.clock, maxTime/60, Pair("min", "Highest time")),
                    Triple(R.drawable.elevation, maxElevation, Pair("m", "Elevation")),
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

fun setNotification(mContext: Context) {
    val notificationId = 1

    //this intent link to Notification class
    val intent = Intent(mContext, Notification::class.java)
    intent.putExtra("notification", notificationId)


    val alarmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        PendingIntent.getBroadcast(
            mContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    else PendingIntent.getBroadcast(
        mContext,
        0,
        intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT
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

@Composable
fun Camera(model: ResultViewModel) {
    val TAG = "terveyshelppi"
    val mContext = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val data by model.getInfo().observeAsState()

    if (data != null) {
        bitmap = BitmapFactory.decodeFile(data!!.avatar)
    }

    //create file
    val fileName = "temp_photo"
    val imgPath = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File.createTempFile(fileName, ".jpg", imgPath)

    //generate content URI
    val photoURI: Uri = FileProvider.getUriForFile(
        mContext,
        "com.example.kari.fileprovider",
        imageFile
    )
    val currentPhotoPath = imageFile.absolutePath

    //launch camera
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                val user = UserData(
                    data!!.name,
                    data!!.weight,
                    data!!.height,
                    data!!.targetSteps,
                    data!!.targetCals,
                    data!!.targetHours,
                    data!!.heartRate,
                    data!!.totalDistance,
                    data!!.totalCalories,
                    data!!.totalSteps,
                    data!!.totalHours,
                    currentPhotoPath,
                    data!!.stepBeginOfDay,
                    data!!.day
                )
                model.updateInfo(user)
            } else Log.d(TAG, "ProfilePage: photo not taken")
        }
    if (bitmap == null) {
        Button(
            onClick = {
                launcher.launch(photoURI)
            },
            modifier = Modifier.padding(top = 20.dp).size(100.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = button
            ),
        ) {
            Image(
                painterResource(id = R.drawable.add),
                "",
                modifier = Modifier.size(40.dp)
            )
        }
    } else {
        Log.d(TAG, "Camera: bitmap: $bitmap")
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }
    }
}