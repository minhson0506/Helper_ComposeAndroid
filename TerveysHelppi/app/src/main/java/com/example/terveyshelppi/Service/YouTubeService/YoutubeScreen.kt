package com.example.terveyshelppi.Service.YouTubeService


import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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

fun searchOnYoutube(keywords: String, model: ResultViewModel){
    val TAG = "terveyshelppi"
    YoutubeApi.apiSearchInstance().search(keywords).enqueue(object : Callback<SearchResponse> {
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
}

@ExperimentalFoundationApi
@Composable
fun YoutubeScreen(model: ResultViewModel) {
    val TAG = "terveyshelppi"
    Log.d(TAG, "YoutubeScreen: start to find video from videoid")

    var displayYoutube by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }
    val title : String? by model.title.observeAsState(null)
    val title1 : String? by model.title1.observeAsState(null)
    val title2: String? by model.title2.observeAsState(null)
    val title3 : String? by model.title3.observeAsState(null)
    val title4 : String? by model.title4.observeAsState(null)

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
        if (result != null) {
            val videoId = result!!.map{it.id.videoId}
            Log.d(TAG, "start to load video")
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
                    val url = "oembed?url=youtube.com/watch?v=${videoId[i]}&format=json"
                    YoutubeApi.apiTitleInstance().searchTitle(url).enqueue(object : Callback<TitleResponse> {
                        override fun onResponse(
                            call: Call<TitleResponse>,
                            response: Response<TitleResponse>,
                        ) {
                            Log.d(TAG, "onResponse: url get title is ${response.raw().request.url}.")
                            Log.d(TAG, "onResponse title: ${response.isSuccessful}")
                            val result = response.body()?.title
                            Log.d(TAG, "onResponse title: $result")
                            when(i) {
                                0 -> model.title.postValue(result)
                                1 -> model.title1.postValue(result)
                                2 -> model.title2.postValue(result)
                                3 -> model.title3.postValue(result)
                                4 -> model.title4.postValue(result)
                            }

                        }

                        override fun onFailure(call: Call<TitleResponse>, t: Throwable) {
                            Log.d(TAG, "onResponse fail: url is ${t.message}")
                            //Log.d(TAG, "onResponse fail: url is ${(t as HttpException).response()?.raw()?.request?.url}")
                            Log.d(TAG, "response onFailure when get response from youtube: ")
                        }
                    })
                    when (i) {
                        0 -> if (title != null) Text(text = "$title")
                        1 -> if (title1 != null) Text(text = "$title1")
                        2 -> if (title2 != null) Text(text = "$title2")
                        3 -> if (title3 != null) Text(text = "$title3")
                        4 -> if (title4 != null) Text(text = "$title4")
                    }
                }
            }

        }


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