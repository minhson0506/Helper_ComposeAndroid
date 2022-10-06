package com.example.terveyshelppi.Service.YouTubeService

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultViewModel: ViewModel() {
    // store data of searching
    val result = MutableLiveData<List<SearchResponse.Item>>(null)

    //store data of title
    private val repository: TitleYoutubeApi.WebServiceRepository = TitleYoutubeApi.WebServiceRepository()
    val title = MutableLiveData<String>()

    fun getTitle(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val titleSearch = repository.search(id)
            title.postValue(titleSearch.title)
        }
    }
}