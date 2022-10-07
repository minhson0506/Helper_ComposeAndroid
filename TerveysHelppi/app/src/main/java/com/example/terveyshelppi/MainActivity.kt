package com.example.terveyshelppi

import android.app.Application
import android.os.Bundle
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
import com.example.terveyshelppi.Components.*
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme
import com.example.terveyshelppi.ui.theme.regular

class MainActivity : AppCompatActivity() {
    companion object {
        private lateinit var model: ResultViewModel
    }

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            model = ResultViewModel(application)
            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController, startDestination = "landingPage") {
                        composable("landingPage") {
                            LandingPage(navController = navController, application)
                        }
                        composable("details") {
                            InfoLanding(navController = navController, application)
                        }
                        composable("main") {
                            MainScreen(model = model, application)
                        }

                    }
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

@ExperimentalFoundationApi
@Composable
fun NavigationGraph(navController: NavHostController, model: ResultViewModel, application: Application) {
    NavHost(navController, startDestination = BottomNavItem.Home.screen_route) {
        composable(BottomNavItem.Fitness.screen_route) {
            FitnessPage(model = model)
        }
        composable(BottomNavItem.Home.screen_route) {
            MainPage(application, navController)
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
fun MainScreen(model: ResultViewModel, application: Application) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationGraph(navController = navController, model = model, application)
            }
        }
    )
}