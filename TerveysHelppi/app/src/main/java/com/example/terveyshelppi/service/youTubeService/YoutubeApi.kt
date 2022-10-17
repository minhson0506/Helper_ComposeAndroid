package com.example.terveyshelppi.service.youTubeService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YoutubeApi {
    fun apiSearchInstance(): SearchApiService {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchApiService::class.java)
    }

    private const val url = "https://www.youtube.com/"
    fun apiTitleInstance(): SearchTitleApiService {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchTitleApiService::class.java)
    }
}