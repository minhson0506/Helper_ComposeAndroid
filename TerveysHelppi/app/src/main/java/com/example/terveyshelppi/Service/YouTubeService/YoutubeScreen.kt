package com.example.terveyshelppi.Service.YouTubeService

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.InputType
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import coil.compose.rememberAsyncImagePainter
import com.example.terveyshelppi.BuildConfig
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.foundation.lazy.items
import com.android.volley.toolbox.StringRequest
import com.google.gson.JsonObject
import org.json.JSONObject
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState


import com.android.volley.VolleyError
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URLConnection


fun searchOnYoutube(keywords: String, model: ResultViewModel): MutableList<String> {
    val TAG = "terveyshelppi"
    var resultId: MutableList<String> = mutableListOf()
    YoutubeApi.apiInstance().search(keywords).enqueue(object : Callback<SearchResponse> {
        override fun onResponse(
            call: Call<SearchResponse>,
            response: Response<SearchResponse>,
        ) {
            Log.d(TAG, "onResponse: ${response.isSuccessful}")
            val result = response.body()?.items
            Log.d(TAG, "onResponse: $result")
            model.result.postValue(result)
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            Log.d(TAG, "onFailure when get response from youtube: ")
        }
    })
    return resultId
}

@ExperimentalFoundationApi
@Composable
fun YoutubeScreen(videoId: List<String>) {
    val TAG = "terveyshelppi"

    Log.d(TAG, "YoutubeScreen: start to find video from videoid")
    Log.d(TAG, "YoutubeScreen: id for play $videoId")

    var displayYoutube by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }

    val viewModel = ResultViewModel()
    val title by viewModel.title.observeAsState()

    if (displayYoutube) playVideo(videoId = id)
    for (i in 0..4) {
        if (videoId[i] != null) {
            Image(
                painter = rememberAsyncImagePainter("http://img.youtube.com/vi/${videoId[i]}/0.jpg"),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clickable {
                        displayYoutube = !displayYoutube
                        id = videoId[i]
                    }
            )

            viewModel.getTitle(videoId[i])
            Log.d(TAG, "YoutubeScreen: title is $title")
//            title?.let { Text(it) }
        }
    }
}

fun parseJsonData(string: String) {
    val TAG = "terveyshelppi"
    try {
        val jsonObject = JSONObject(string)
    } catch (error: IOException) {
        Log.d(TAG, "parseJsonData: error when convert json to string")
    }
}

@Composable
fun playVideo(videoId: String) {
    val TAG = "terveyshelppi"
    val context = LocalContext.current
    val api_key = BuildConfig.YOUTUBE_API_KEY

    AndroidView(factory = {
        val fm = (context as AppCompatActivity).supportFragmentManager
        val view = FragmentContainerView(it).apply {
            id = com.example.terveyshelppi.R.id.ytPlayer

        }
        val fragment = YouTubePlayerSupportFragmentXKt().apply {
            initialize(api_key,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        result: YouTubeInitializationResult,
                    ) {
                        Log.d(TAG, "onInitializationFailure: error to play video")
                    }

                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        player: YouTubePlayer,
                        wasRestored: Boolean,
                    ) {
                        Log.d(TAG, "onInitializationSuccess: string 0")
                        if (!wasRestored) {
                            player.cueVideo(videoId)
                        }
                    }
                })
        }
        fm.commit {
            setReorderingAllowed(true)
            add(com.example.terveyshelppi.R.id.ytPlayer, fragment)
        }
        view
    })
}