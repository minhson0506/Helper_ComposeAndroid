package com.example.terveyshelppi

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.preference.PreferenceManager
import com.example.terveyshelppi.components.*
import com.example.terveyshelppi.service.GattClientCallback
import com.example.terveyshelppi.service.map.GetLocation
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.service.roomDB.UserData
import com.example.terveyshelppi.service.ShowSensorData
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.regular
import org.osmdroid.config.Configuration
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {
    private val tag = "terveyshelppi"

    companion object {
        private lateinit var model: ResultViewModel
        private lateinit var sm: SensorManager
        private var stepSensor: Sensor? = null
        private var sTemperature: Sensor? = null
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // init view model
        model = ResultViewModel(application)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        //step counter & temperature sensor
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sm.getSensorList(Sensor.TYPE_ALL).forEach {
            Log.d(tag, "sensor is ${it.name}")
        }
        sTemperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        stepSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // check all permissions
        hasPermissions(bluetoothAdapter = bluetoothAdapter!!, activity = this)

        // check heart rate sensor and connect
        for (btDev in bluetoothAdapter?.bondedDevices!!) {
            Log.d(tag, "bluetooth device bonded is: : ${btDev.name}")
            if (btDev.name.startsWith("Polar")) {
                Log.d(tag, "connected to heart rate sensor")
                val bluetoothGatt =
                    btDev.connectGatt(this, false, GattClientCallback(model = model))
                Log.d(tag, "connect Polar is ${bluetoothGatt.connect()}")
                break
            }
        }

        //init map
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        setContent {
            val navController = rememberNavController()

            //get location map
            GetLocation(context = this, activity = this@MainActivity, model = model)

            // reset data in the beginning of the day
            ResetRoomData(model)

            // save sensor data
            ShowSensorData(model, application)

            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController, startDestination = "landingPage") {
                        composable("landingPage") {
                            LandingPage(navController = navController, model)
                        }
                        composable("details") {
                            InfoLanding(navController = navController, model)
                        }
                        composable("main") {
                            MainScreen(
                                model = model,
                                this@MainActivity
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if (p0.sensor == sTemperature) {
            model.updateTempValue(p0.values[0].toString())
            Log.d(tag, "onSensorChanged: temp ${p0.values[0]}")
        }
        if (p0.sensor == stepSensor) {
            model.updateStepValue(
                getString(
                    R.string.sensor_val,
                    p0.values[0],
                )
            )
            Log.d(tag, "onSensorChanged: step ${p0.values[0]}")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(tag, "onAccuracyChanged ${p0?.name}: $p1")
    }

    override fun onResume() {
        super.onResume()
        if (sTemperature == null) {
            // show toast message, if there is no sensor in the device
            Toast.makeText(
                this,
                "No temperature sensor detected on this device",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // register listener with sensorManager
            sm.registerListener(this, sTemperature, SensorManager.SENSOR_DELAY_UI)
        }
        if (stepSensor == null) {
            // show toast message, if there is no sensor in the device
            Toast.makeText(this, "No step sensor detected on this device", Toast.LENGTH_SHORT)
                .show()
        } else {
            // register listener with sensorManager
            sm.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }
}

// reset data every new day
@Composable
fun ResetRoomData(model: ResultViewModel) {
    val tag = "terveyshelppi"

    val userDataFetch by model.getInfo().observeAsState(null)
    if (userDataFetch != null) {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        if (userDataFetch?.day != Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            Log.d(tag, "getInfo: reset counter for beginning of the day")
            Log.d(tag, "ResetRoomData: $userDataFetch")
            val user = UserData(
                userDataFetch!!.name,
                userDataFetch!!.weight,
                userDataFetch!!.height,
                userDataFetch!!.targetSteps,
                userDataFetch!!.targetCals,
                userDataFetch!!.targetHours,
                0,
                0,
                0,
                userDataFetch!!.totalSteps,
                0,
                userDataFetch!!.avatar,
                userDataFetch!!.totalSteps,
                day
            )
            model.updateInfo(user)
            model.distance.postValue(0.0)
            model.hours.postValue(0.0)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun hasPermissions(
    bluetoothAdapter: BluetoothAdapter,
    activity: AppCompatActivity,
): Boolean {
    val tag = "terveyshelppi"
    if (!bluetoothAdapter.isEnabled) {
        Log.d(tag, "No Bluetooth LE capability")
        return false
    } else {
        if (
            (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d(tag, "No permission")
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ), 1
            )
            if (Build.VERSION.SDK_INT >= 31) {
                while ((activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                    (activity.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) ||
                    (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) ||
                    (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ) {
                    Log.d(tag, "hasPermissions: wait to permission granted")
                }
            } else while ((activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (activity.checkSelfPermission(Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) ||
                (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ) {
                Log.d(tag, "hasPermissions: wait to permission granted")
            }
            return true
        }

        Log.i(tag, "permissions ok")
        return true
    }
}


sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.home, "home")
    object Fitness : BottomNavItem("Fitness", R.drawable.fitness, "fitness")
    object Profile : BottomNavItem("Profile", R.drawable.profile, "profile")

}

@ExperimentalFoundationApi
@Composable
fun NavigationGraph(
    navController: NavHostController,
    model: ResultViewModel,
    activity: AppCompatActivity,
) {
    val heartRate by model.graph.observeAsState()

    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage(model = model, activity = activity)
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage(navController, model)
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfilePage(navController, model)
        }
        composable("graph-heartRate") {
            heartRate?.let { it1 -> Graph(it1, navController) }
        }
        composable("daily") {
            DailyActivity(model, navController)
        }
        composable("update") {
            UpdateProfile(model, navController)
        }
        composable("exercise") {
            Exercise(navController, model)
        }
        composable("exercise_result") {
            ExerciseResult(navController, model)
        }
        composable("history") {
            ExerciseHistory(navController, model)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Fitness,
        BottomNavItem.Profile,

        )
    BottomNavigation(
        backgroundColor = Color.Black,
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Image(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(20.dp),
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontFamily = regular
                    )
                },
                alwaysShowLabel = true,
                selected = false,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MainScreen(
    model: ResultViewModel,
    activity: AppCompatActivity,
) {
    val navController = rememberNavController()
    var showBottomBar by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    showBottomBar = when (navBackStackEntry?.destination?.route) {
        "exercise" -> false
        "exercise_result" -> false
        "daily" -> false
        "graph-heartRate" -> false
        "history" -> false
        "update" -> false
        else -> true
    }

    Scaffold(
        bottomBar = { if (showBottomBar) BottomNavigationBar(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(
                    navController = navController,
                    model = model,
                    activity = activity
                )
            }
        }
    )
}