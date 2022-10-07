package com.example.terveyshelppi.Service.YouTubeService


import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import coil.compose.rememberAsyncImagePainter
import com.example.terveyshelppi.BuildConfig
import com.example.terveyshelppi.R
import com.example.terveyshelppi.ui.theme.*
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun searchOnYoutube(keywords: String, model: ResultViewModel) {
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
    val title: String? by model.title.observeAsState(null)
    val title1: String? by model.title1.observeAsState(null)
    val title2: String? by model.title2.observeAsState(null)
    val title3: String? by model.title3.observeAsState(null)
    val title4: String? by model.title4.observeAsState(null)

    var input by remember { mutableStateOf("") }
    val result: List<SearchResponse.Item>? by model.result.observeAsState(null)
    var bool by remember { mutableStateOf(true) }


    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, ), verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = {
                Text(
                    stringResource(id = R.string.search),
                    fontFamily = regular,
                    fontSize = 12.sp
                )
            },
            modifier = Modifier
                .padding(end = 10.dp)
                .width(210.dp)
                .border(
                    BorderStroke(
                        2.dp, brush = Brush.verticalGradient(
                            colors = listOf(
                                main1, main2
                            )
                        )
                    ), shape = RoundedCornerShape(10)
                )
                .background(Color.White, shape = RoundedCornerShape(10))
        )
        Button(
            onClick = {
                displayYoutube = false
                searchOnYoutube(input, model)
                input = ""
                bool = false
                Log.d(TAG, "Greeting: ${result.toString()}")
            },
            modifier = Modifier
                .padding(end = 10.dp)
                .size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = button),
        ) {
            Image(
                painterResource(id = R.drawable.search),
                "",
                modifier = Modifier.size(20.dp)
            )
        }
        Button(
            onClick = {
                displayYoutube = false
                model.result.postValue(null)
                bool = true
            },
            modifier = Modifier
                .padding(end = 20.dp)
                .size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = button),
        ) {
            Image(
                painterResource(id = R.drawable.cancel),
                "",
                modifier = Modifier.size(20.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //display search or suggested videos
        if (bool) {
            Text(
                text = "Suggested for you", color = Color.White,
                fontFamily = semibold,
                fontSize = 16.sp
            )
            val items = listOf(
                SearchResponse.Item("", SearchResponse.Item.Id("", "GLy2rYHwUqY"), ""),
                SearchResponse.Item("", SearchResponse.Item.Id("", "XcZ6ude_P3Y"), ""),
                SearchResponse.Item("", SearchResponse.Item.Id("", "4pKly2JojMw"), ""),
                SearchResponse.Item("", SearchResponse.Item.Id("", "VCAwwX3WiA0"), ""),
                SearchResponse.Item("", SearchResponse.Item.Id("", "w9aXIcuPWqk"), "")
            )

            model.result.postValue(items)
        }

        val videoId = result?.map { it.id.videoId }

        Log.d(TAG, "start to load video")

        if (displayYoutube) playVideo(videoId = id)
        for (i in 0..4) {
            if (videoId != null) {
                Image(
                    painter = rememberAsyncImagePainter("http://img.youtube.com/vi/${videoId[i]}/0.jpg"),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(0.dp)
                        .size(300.dp)
                        .clickable {
                            displayYoutube = !displayYoutube
                            id = videoId[i]
                        }
                )
                val url = "oembed?url=youtube.com/watch?v=${videoId[i]}&format=json"
                YoutubeApi.apiTitleInstance().searchTitle(url)
                    .enqueue(object : Callback<TitleResponse> {
                        override fun onResponse(
                            call: Call<TitleResponse>,
                            response: Response<TitleResponse>,
                        ) {
                            Log.d(
                                TAG,
                                "onResponse: url get title is ${response.raw().request.url}."
                            )
                            Log.d(TAG, "onResponse title: ${response.isSuccessful}")
                            val result = response.body()?.title
                            Log.d(TAG, "onResponse title: $result")
                            when (i) {
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
                    0 -> if (title != null) Text(
                        text = "$title",
                        color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(0.dp)
                    )
                    1 -> if (title1 != null) Text(
                        text = "$title1", color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp, textAlign = TextAlign.Center
                    )
                    2 -> if (title2 != null) Text(
                        text = "$title2", color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp, textAlign = TextAlign.Center
                    )
                    3 -> if (title3 != null) Text(
                        text = "$title3", color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp, textAlign = TextAlign.Center
                    )
                    4 -> if (title4 != null) Text(
                        text = "$title4", color = Color.White,
                        fontFamily = semibold,
                        fontSize = 14.sp, textAlign = TextAlign.Center
                    )
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
    Log.d(TAG, "playVideo: with id $videoId")
    AndroidView(factory = {
        val fm = (context as AppCompatActivity).supportFragmentManager
        val view = FragmentContainerView(it).apply {
            id = R.id.ytPlayer

        }
        val fragment = YouTubePlayerSupportFragmentXKt().apply {
            initialize(api_key,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        result: YouTubeInitializationResult,
                    ) {
                        Log.d(TAG, "onInitializationFailure: error to play video ${result.name}")
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
            add(R.id.ytPlayer, fragment)
        }
        view
    })
}