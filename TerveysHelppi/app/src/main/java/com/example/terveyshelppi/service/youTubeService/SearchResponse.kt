package com.example.terveyshelppi.service.youTubeService

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items")
    val items: List<Item>,
) {
    data class Item(
        @SerializedName("etag")
        val etag: String,
        @SerializedName("id")
        val id: Id,
        @SerializedName("kind")
        val kind: String,
    ) {
        data class Id(
            @SerializedName("kind")
            val kind: String,
            @SerializedName("videoId")
            val videoId: String,
        )
    }
}

data class TitleResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val author_name: String,
    @SerializedName("author_url")
    val author_url: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("version")
    val version: String,
    @SerializedName("provider_name")
    val provider_name: String,
    @SerializedName("provider_url")
    val provider_url: String,
    @SerializedName("thumbnail_height")
    val thumbnail_height: Int,
    @SerializedName("thumbnail_width")
    val thumbnail_width: Int,
    @SerializedName("thumbnail_url")
    val thumbnail_url: String,
    @SerializedName("html")
    val html: String
)