package com.example.terveyshelppi

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import com.example.terveyshelppi.Components.*
import com.example.terveyshelppi.Service.GattClientCallback
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.regular
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val TAG = "terveyshelppi"
    var model = ResultViewModel()
    private var bluetoothAdapter: BluetoothAdapter? = null

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // check heart rate sensor and connect

            for (btDev in bluetoothAdapter?.bondedDevices!!) {
                Log.d(TAG, "bluetooth device bonded is: : ${btDev.name}")
                if (btDev.name.startsWith("Polar")) {
                    Log.d(TAG, "connected to heart rate sensor")
                    val bluetoothGatt = btDev.connectGatt(this, false, GattClientCallback(model = model))
                    Log.d(TAG, "connect Polar is ${bluetoothGatt.connect()}")
                    break;
                }
            }

        setContent {
            hasPermissions(bluetoothAdapter = bluetoothAdapter!!, activity = this)
            val navController = rememberNavController()
            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController, startDestination = "landingPage") {
                        composable("landingPage") {
                            LandingPage(navController = navController)
                        }
                        composable("details") {
                            InfoLanding(navController = navController)
                        }
                        composable("main") {
                            MainScreen(model = model)
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
        if ((activity.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
            (activity.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.d(TAG, "No fine location access")
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT
                ),
                1
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
fun NavigationGraph(navController: NavHostController, model: ResultViewModel) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage(model = model)
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage(model = model)
        }
        composable(BottomNavItem.Profile.screen_route) {
            ProfilePage()
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
fun MainScreen(model: ResultViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = {
            NavigationGraph(navController = navController, model = model)
        }
    )
}