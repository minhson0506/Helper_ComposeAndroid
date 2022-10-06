package com.example.terveyshelppi

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel
import com.example.terveyshelppi.Service.YouTubeService.YoutubeScreen
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme

class MainActivity : AppCompatActivity() {
    var model = ResultViewModel()
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    YoutubeScreen(model)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Greeting(model: ResultViewModel) {
}

