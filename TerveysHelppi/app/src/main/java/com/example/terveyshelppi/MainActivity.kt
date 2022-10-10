package com.example.terveyshelppi

import android.app.Application
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.view.WindowManager
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.terveyshelppi.Components.*
import com.example.terveyshelppi.Service.GattClientCallback
import com.example.terveyshelppi.Service.Sensors.SensorViewModel
import com.example.terveyshelppi.Service.Sensors.ShowSensorData
import com.example.terveyshelppi.Service.GetLocation
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.regular
import org.osmdroid.config.Configuration
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), SensorEventListener {
    val TAG = "terveyshelppi"

    companion object {
        private lateinit var model: ResultViewModel
        private var sensorViewModel = SensorViewModel()
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

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        //step counter & temperature sensor
        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sm.getSensorList(Sensor.TYPE_ALL).forEach {
            Log.d(TAG, "sensor is ${it.name}")
        }
        sTemperature = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        stepSensor = sm.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)


        hasPermissions(bluetoothAdapter = bluetoothAdapter!!, activity = this)

        // check heart rate sensor and connect
        model = ResultViewModel(application)
        for (btDev in bluetoothAdapter?.bondedDevices!!) {
            Log.d(TAG, "bluetooth device bonded is: : ${btDev.name}")
            if (btDev.name.startsWith("Polar")) {
                Log.d(TAG, "connected to heart rate sensor")
                val bluetoothGatt =
                    btDev.connectGatt(this, false, GattClientCallback(model = model))
                Log.d(TAG, "connect Polar is ${bluetoothGatt.connect()}")
                break;
            }
        }

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))


        setContent {
            val navController = rememberNavController()
            ShowSensorData(sensorViewModel, application)

            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GetLocation(context = this, activity = this@MainActivity, model = model)

                    NavHost(navController, startDestination = "landingPage") {
                        composable("landingPage") {
                            LandingPage(navController = navController, application)
                        }
                        composable("details") {
                            InfoLanding(navController = navController, application)
                        }
                        composable("main") {
                            MainScreen(
                                model = model,
                                application,
                                this@MainActivity,
                                sensorViewModel = sensorViewModel
                            )
                        }

                    }
                }
            }
        }
    }

    override fun onSensorChanged(p0: SensorEvent) {
        if (p0.sensor == sTemperature) {
            sensorViewModel.updateTempValue(p0.values[0].toString())
            Log.d(TAG, "onSensorChanged: temp ${p0.values[0]}")
        }
        if (p0.sensor == stepSensor) {
            sensorViewModel.updateStepValue(
                getString(
                    R.string.sensor_val,
                    p0.values[0],
                )
            )
            Log.d(TAG, "onSensorChanged: step ${p0.values[0]}")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d(TAG, "onAccuracyChanged ${p0?.name}: $p1")
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

@RequiresApi(Build.VERSION_CODES.S)
fun hasPermissions(
    bluetoothAdapter: BluetoothAdapter,
    activity: AppCompatActivity,
): Boolean {
    val TAG = "terveyshelppi"
    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
        Log.d(TAG, "No Bluetooth LE capability")
        return false
    } else {
        if ((activity.checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) || (activity.checkSelfPermission(
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d(TAG, "No permission")
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION,
                ), 1
            ); return true
        }
        Log.i(TAG, "permissions ok")
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
    application: Application,
    activity: AppCompatActivity,
    sensorViewModel: SensorViewModel
) {
    val heartRate by model.graph.observeAsState()

    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage(model = model, activity = activity)
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage(application, navController, model, sensorViewModel)
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfilePage()
        }
        composable("exercise") {
            Exercise(navController, model)
        }
        composable("graph-heartRate") {
            heartRate?.let { it1 -> Graph(it1) }
        }
        composable("daily") {
            DailyActivity(sensorViewModel, model)
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
                label = { Text(text = item.title, color = Color.White, fontFamily = regular) },
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
    application: Application,
    activity: AppCompatActivity,
    sensorViewModel: SensorViewModel
) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(
                    navController = navController,
                    model = model,
                    application = application,
                    activity = activity,
                    sensorViewModel = sensorViewModel
                )
            }
        }
    )
}