package com.example.terveyshelppi.components

import android.annotation.SuppressLint
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
import com.example.terveyshelppi.libraryComponent.TextModifiedWithString
import com.example.terveyshelppi.service.notification.Notification
import com.example.terveyshelppi.service.roomDB.UserData
import com.example.terveyshelppi.service.ResultViewModel
import java.io.File
import java.util.*
import kotlin.math.round

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfilePage(navControler: NavController, model: ResultViewModel) {
    val mContext = LocalContext.current

    // state to display dropdown menu
    var mDisplayMenu by remember { mutableStateOf(false) }

    // exercise and user data from Room
    val exerciseData by model.getAllExercises().observeAsState()
    val userData by model.getInfo().observeAsState()

    var user by remember { mutableStateOf("") }
    var targetSteps by remember { mutableStateOf(0) }
    var targetHours by remember { mutableStateOf(0) }
    var targetCals by remember { mutableStateOf(0) }
    var maxDistance by remember { mutableStateOf(0) }
    var maxTime by remember { mutableStateOf(0L) }
    var maxCalories by remember { mutableStateOf(0) }
    var maxElevation by remember { mutableStateOf(0) }
    var maxSpeed by remember { mutableStateOf(0.0) }

    if (userData != null) {
        user = userData?.name.toString()
        targetSteps = userData!!.targetSteps
        targetCals = userData!!.targetCals
        targetHours = userData!!.targetHours
    }

    // get highest values from exercises
    if (exerciseData != null) {
        maxDistance = exerciseData!!.maxOfOrNull { it.distance } ?: 0
        maxTime = exerciseData!!.maxOfOrNull { it.activeTime } ?: 0
        maxCalories = exerciseData!!.maxOfOrNull { it.calories } ?: 0
        maxElevation = exerciseData!!.maxOfOrNull { it.elevation } ?: 0
        maxSpeed = exerciseData!!.maxOfOrNull { it.averageSpeed } ?: 0.0
    }

    // data for grid view (goals)
    val goalArray = listOf(
        Triple(R.drawable.step, targetSteps, "steps/day"),
        Triple(R.drawable.cal, targetCals, "cals/day"),
        Triple(R.drawable.clock, targetHours, "mins/day"),
    )

    // data for grid view (personal best)
    val textArray = listOf(
        Triple(R.drawable.distance, maxDistance, Pair("m", "Distance")),
        Triple(R.drawable.cal, maxCalories, Pair("Cal", "Most calories")),
        Triple(R.drawable.speed, round(maxSpeed), Pair("km/h", "Highest speed")),
        Triple(R.drawable.clock, maxTime / 60, Pair("min", "Highest time")),
        Triple(R.drawable.elevation, maxElevation, Pair("m", "Elevation")),
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
            horizontalAlignment = CenterHorizontally
        ) {
            // function to take avatar picture
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
                                TextModifiedWithString(
                                    string = it.third,
                                    color = smallText,
                                    font = regular
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.pb),
                        color = Color.White,
                        modifier = Modifier.padding(top = 15.dp, start = 10.dp, bottom = 20.dp),
                        fontFamily = semibold,
                        fontSize = 16.sp
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

@SuppressLint("UnspecifiedImmutableFlag")
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
    val tag = "terveyshelppi"
    val mContext = LocalContext.current

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // get user data from Room
    val userData by model.getInfo().observeAsState()
    if (userData != null) {
        bitmap = BitmapFactory.decodeFile(userData!!.avatar)
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

                // add picture path to Room
                val user = UserData(
                    userData!!.name,
                    userData!!.weight,
                    userData!!.height,
                    userData!!.targetSteps,
                    userData!!.targetCals,
                    userData!!.targetHours,
                    userData!!.heartRate,
                    userData!!.totalDistance,
                    userData!!.totalCalories,
                    userData!!.totalSteps,
                    userData!!.totalHours,
                    currentPhotoPath,
                    userData!!.stepBeginOfDay,
                    userData!!.day
                )
                model.updateInfo(user)
            } else Log.d(tag, "ProfilePage: photo not taken")
        }
    if (bitmap == null) {
        Button(
            onClick = {
                launcher.launch(photoURI)
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .size(100.dp),
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
        Log.d(tag, "Camera: bitmap: $bitmap")
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