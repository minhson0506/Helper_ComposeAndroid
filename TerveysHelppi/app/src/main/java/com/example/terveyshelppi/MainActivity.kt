package com.example.terveyshelppi

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
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
import com.example.terveyshelppi.ui.theme.TerveysHelppiTheme

class MainActivity : AppCompatActivity() {
    var model = ResultViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerveysHelppiTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting(model)
                }
            }
        }
    }
}

@Composable
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

