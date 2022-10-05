package com.example.terveyshelppi.Service.YouTubeService

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel: ViewModel() {
    val result = MutableLiveData<List<SearchResponse.Item>>(null)
}