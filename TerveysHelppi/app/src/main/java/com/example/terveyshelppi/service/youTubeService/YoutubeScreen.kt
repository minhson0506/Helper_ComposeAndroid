package com.example.terveyshelppi.service.youTubeService


import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import coil.compose.rememberAsyncImagePainter
import com.example.terveyshelppi.BuildConfig
import com.example.terveyshelppi.R
import com.example.terveyshelppi.libraryComponent.TextModifiedWithPaddingTop
import com.example.terveyshelppi.service.ResultViewModel
import com.example.terveyshelppi.ui.theme.*
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun searchOnYoutube(keywords: String, model: ResultViewModel) {
    val tag = "terveyshelppi"
    YoutubeApi.apiSearchInstance().search(keywords).enqueue(object : Callback<SearchResponse> {
        override fun onResponse(
            call: Call<SearchResponse>,
            response: Response<SearchResponse>,
        ) {
            Log.d(tag, "onResponse: ${response.isSuccessful}")
            val result = response.body()?.items
            Log.d(tag, "onResponse: $result")
            model.result.postValue(result)
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            Log.d(tag, "onFailure when get response from youtube: ")
        }
    })
}

@ExperimentalFoundationApi
@Composable
fun YoutubeScreen(model: ResultViewModel, activity: AppCompatActivity) {
    val tag = "terveyshelppi"
    Log.d(tag, "YoutubeScreen: start to find video from videoid")

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
    var shuffle by remember { mutableStateOf(true)}


    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp), verticalAlignment = Alignment.CenterVertically
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
                Log.d(tag, "Greeting: ${result.toString()}")
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
                shuffle = true
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
            .padding(top = 10.dp, bottom = 10.dp)
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
            if (shuffle) {
                // data for suggestion
                val items = listOf(
                    //yoga
                    SearchResponse.Item("", SearchResponse.Item.Id("", "GLy2rYHwUqY"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "XcZ6ude_P3Y"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "4pKly2JojMw"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "VCAwwX3WiA0"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "w9aXIcuPWqk"), ""),
                    //gym
                    SearchResponse.Item("", SearchResponse.Item.Id("", "2pLT-olgUJs"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "uUKAYkQZXko"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "0arpNtjXuLY"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "pMTWS3CkBG0"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "GfVJ7gCGNOA"), ""),
                    //diet
                    SearchResponse.Item("", SearchResponse.Item.Id("", "0KFKLTkvZNI"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "8BKbu_s8p1Q"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "CxktmQ3zJOA"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "vmdITEguAnE"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "wpLJXHUyvyM"), ""),
                    //stretching
                    SearchResponse.Item("", SearchResponse.Item.Id("", "flDQ-Av5DZ8"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "YfCK3uOz1r4"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "itJE4neqDJw"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "I9ZRSpLTSu8"), ""),
                    SearchResponse.Item("", SearchResponse.Item.Id("", "qULTwquOuT4"), "")
                )

                val suggestionsList = items.shuffled()
                model.result.postValue(listOf(suggestionsList[0], suggestionsList[1], suggestionsList[2], suggestionsList[3], suggestionsList[4]))
                shuffle = false
            }
        }

        val videoId = result?.map { it.id.videoId }

        Log.d(tag, "start to load video")

        //play youtube video
        if (displayYoutube) PlayVideo(videoId = id) else {
            val fragment: Fragment? =
                activity.supportFragmentManager.findFragmentById(R.id.ytPlayer)
            if (fragment != null)
                activity.supportFragmentManager.beginTransaction().remove(fragment).commit()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            for (i in 0..4) {
                if (videoId != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
                        backgroundColor = card,
                        elevation = 4.dp
                    ) {
                        // display thumbnail of video
                        Image(
                            painter = rememberAsyncImagePainter("http://img.youtube.com/vi/${videoId[i]}/0.jpg"),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .height(300.dp)
                                .fillMaxWidth()
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
                                        tag,
                                        "onResponse: url get title is ${response.raw().request.url}."
                                    )
                                    Log.d(tag, "onResponse title: ${response.isSuccessful}")
                                    val resultResponse = response.body()?.title
                                    Log.d(tag, "onResponse title: $resultResponse")
                                    when (i) {
                                        0 -> model.title.postValue(resultResponse)
                                        1 -> model.title1.postValue(resultResponse)
                                        2 -> model.title2.postValue(resultResponse)
                                        3 -> model.title3.postValue(resultResponse)
                                        4 -> model.title4.postValue(resultResponse)
                                    }

                                }

                                override fun onFailure(call: Call<TitleResponse>, t: Throwable) {
                                    Log.d(tag, "onResponse fail: url is ${t.message}")
                                    //Log.d(TAG, "onResponse fail: url is ${(t as HttpException).response()?.raw()?.request?.url}")
                                    Log.d(
                                        tag,
                                        "response onFailure when get response from youtube: "
                                    )
                                }
                            })
                        // display title of video
                        when (i) {
                            0 -> if (title != null) TextModifiedWithPaddingTop(string = title)
                            1 -> if (title1 != null) TextModifiedWithPaddingTop(string = title1)
                            2 -> if (title2 != null) TextModifiedWithPaddingTop(string = title2)
                            3 -> if (title3 != null) TextModifiedWithPaddingTop(string = title3)
                            4 -> if (title4 != null) TextModifiedWithPaddingTop(string = title4)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlayVideo(videoId: String) {
    val tag = "terveyshelppi"
    val context = LocalContext.current
    val apiKey = BuildConfig.API_KEY
    Log.d(tag, "playVideo: with id $videoId")

    AndroidView(factory = {
        val fm = (context as AppCompatActivity).supportFragmentManager
        val view = FragmentContainerView(it).apply {
            id = R.id.ytPlayer

        }
        val fragment = YouTubePlayerSupportFragmentXKt().apply {
            initialize(apiKey,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        result: YouTubeInitializationResult,
                    ) {
                        Log.d(tag, "onInitializationFailure: error to play video ${result.name}")
                    }

                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        player: YouTubePlayer,
                        wasRestored: Boolean,
                    ) {
                        Log.d(tag, "onInitializationSuccess: string 0")
                        if (!wasRestored) {
                            player.loadVideo(videoId)
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