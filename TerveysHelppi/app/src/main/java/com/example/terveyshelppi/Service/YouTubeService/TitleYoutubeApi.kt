package com.example.terveyshelppi.Service.YouTubeService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object TitleYoutubeApi {
    private const val url = "https://www.youtube.com/"

    object Model {
        data class Info(
            val title: String,
            val author_name: String,
            val author_url: String,
            val type: String,
            val height: Int,
            val width: Int,
            val version: String,
            val provider_name: String,
            val provider_url: String,
            val thumbnail_height: Int,
            val thumbnail_width: Int,
            val thumbnail_url: String,
            val html: String,
        )
    }

    interface ServiceApi {
        @GET("oembed?url=youtube.com/")
        suspend fun searchTitle(
            @Query("v") id: String,
            @Query("format") format: String = "json",
        ): Model.Info
    }

    private val retrofit = Retrofit.Builder().baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val serviceApi: ServiceApi = retrofit.create(ServiceApi::class.java)

    class WebServiceRepository() {
        private val call = serviceApi
        suspend fun search(id: String) = call.searchTitle(id)
    }

}