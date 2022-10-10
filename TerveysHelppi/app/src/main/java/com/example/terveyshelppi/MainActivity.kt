package com.example.terveyshelppi

import android.app.Application
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.example.terveyshelppi.Components.*
import com.example.terveyshelppi.Service.GattClientCallback
import com.example.terveyshelppi.Service.GetLocation
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.regular
import org.osmdroid.config.Configuration
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val TAG = "terveyshelppi"

    companion object {
        private lateinit var model: ResultViewModel
    }

    private var bluetoothAdapter: BluetoothAdapter? = null

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        hasPermissions(bluetoothAdapter = bluetoothAdapter!!, activity = this)
        // check heart rate sensor and connect
//        if (androidx.core.app.ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.BLUETOOTH_CONNECT
//            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermissions(
//                arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 1)
//        }
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
                            MainScreen(model = model, application, this@MainActivity)
                        }

                    }
                }
            }
        }
    }
}

fun hasPermissions(bluetoothAdapter: BluetoothAdapter, activity: AppCompatActivity): Boolean {
    val TAG = "terveyshelppi"
    if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
        Log.d(TAG, "No Bluetooth LE capability")
        return false
    } else
        if ((activity.checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d(TAG, "No permission")
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            ); return true // assuming that the user grants permission
        }
    Log.i(TAG, "permissions ok")
    return true
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
) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage(model = model, activity = activity)
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage(application, navController, model)
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfilePage()
        }
        composable("exercise") {
            Exercise(navController)
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
fun MainScreen(model: ResultViewModel, application: Application, activity: AppCompatActivity) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController = navController,
                    model = model,
                    application = application,
                    activity = activity)
            }
        }
    )
}