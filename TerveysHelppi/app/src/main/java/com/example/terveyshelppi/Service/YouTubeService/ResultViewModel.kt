package com.example.terveyshelppi.Service.YouTubeService

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.Entry

class ResultViewModel: ViewModel() {
    val TAG = "terveyshelppi"
    // store data of searching
    val result = MutableLiveData<List<SearchResponse.Item>>(null)

    //store data of title
    val title = MutableLiveData<String>(null)
    val title1 = MutableLiveData<String>(null)
    val title2 = MutableLiveData<String>(null)
    val title3 = MutableLiveData<String>(null)
    val title4 = MutableLiveData<String>(null)

    // store heart rate
    val mBPM = MutableLiveData(0)
    val highmBPM = MutableLiveData(0)
    val lowmBPM = MutableLiveData(300)
    val graph = MutableLiveData(mutableListOf<Entry>())

}