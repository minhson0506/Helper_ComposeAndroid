package com.example.terveyshelppi.Service.YouTubeService

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.example.terveyshelppi.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentXKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
//            result?.forEach { resultId.add(it.id.videoId) }
            model.result.postValue(result)
        }

        override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
            Log.d(TAG, "onFailure when get response from youtube: ")
        }
    })
    return resultId
}

@Composable
fun YoutubeScreen(api_key: String, videoId: String) {
    val TAG = "terveyshelppi"
    val ctx = LocalContext.current
    Log.d(TAG, "YoutubeScreen: start to find video from videoid")
    AndroidView(factory = {
        val fm = (ctx as AppCompatActivity).supportFragmentManager
        val view = FragmentContainerView(it).apply {
            id = R.id.fragment_container_view_tag
        }
        val fragment = YouTubePlayerSupportFragmentXKt().apply {
            initialize(api_key,
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationFailure(
                        provider: YouTubePlayer.Provider,
                        result: YouTubeInitializationResult
                    ) {
                        Log.d(TAG, "onInitializationFailure: error to play video")
                    }

                    override fun onInitializationSuccess(
                        provider: YouTubePlayer.Provider,
                        player: YouTubePlayer,
                        wasRestored: Boolean
                    ) {
                        if (!wasRestored) {
                            player.cueVideo(videoId)
                        }
                    }
                })
        }
        fm.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view_tag, fragment)
        }
        view
    })
}