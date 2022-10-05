package com.example.terveyshelppi

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
<<<<<<< HEAD
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.terveyshelppi.Components.*
=======
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.Service.YouTubeService.SearchResponse
import com.example.terveyshelppi.Service.YouTubeService.YoutubeScreen
import com.example.terveyshelppi.Service.YouTubeService.searchOnYoutube
>>>>>>> f449e88 (basic Youtube service)
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.card
import com.example.terveyshelppi.ui.theme.regular
import com.example.terveyshelppi.ui.theme.semibold
import java.nio.file.Files.size

class MainActivity : AppCompatActivity() {
    var model = ResultViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
<<<<<<< HEAD
                    NavHost(navController, startDestination = "landingPage") {
                        composable("landingPage") {
                            LandingPage(navController = navController)
                        }
                        composable("details") {
                            InfoLanding(navController = navController)
                        }
                        composable("main") {
                            MainScreen()
                        }
                    }

=======
                    Greeting(model)
>>>>>>> f449e88 (basic Youtube service)
                }
            }
        }
    }
}

sealed class BottomNavItem(var title: String, var icon: Int, var screen_route: String) {
    object Home : BottomNavItem("Home", R.drawable.home, "home")
    object Fitness : BottomNavItem("Fitness", R.drawable.fitness, "fitness")
    object Profile : BottomNavItem("Profile", R.drawable.profile, "profile")

}

@Composable
<<<<<<< HEAD
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage()
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage()
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

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = {
            NavigationGraph(navController = navController)
        }
    )
}
=======
fun Greeting(model: ResultViewModel) {

    val TAG = "terveyshelppi"
    val api_key = BuildConfig.YOUTUBE_API_KEY
    var input by remember { mutableStateOf("") }
    val result: List<SearchResponse.Item>? by model.result.observeAsState(null)
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(text = "Hello Android!")
        TextField(value = input, onValueChange = { input = it })
        Button(onClick = {
            searchOnYoutube(input, model)
            Log.d(TAG, "Greeting: ${result.toString()}")
        }) {
            Text(text = "Search")
        }
//        result?.forEach {
        if (result != null) {
            Log.d(TAG, "Greeting: start to load video")
                YoutubeScreen(api_key = api_key, videoId = result!![0].id.videoId)

        }
    }

}

>>>>>>> f449e88 (basic Youtube service)
