package com.example.terveyshelppi.Service.YouTubeService

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terveyshelppi.Service.RoomDB.RoomDB
import com.example.terveyshelppi.Service.RoomDB.RunData
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.RoomDB.WalkData
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry

class ResultViewModel(application: Application): AndroidViewModel(application) {
    val TAG = "terveyshelppi"
    // store data of searching
    val result = MutableLiveData<List<SearchResponse.Item>>(null)

    //store data of title
    val title = MutableLiveData<String>(null)
    val title1 = MutableLiveData<String>(null)
    val title2 = MutableLiveData<String>(null)
    val title3 = MutableLiveData<String>(null)
    val title4 = MutableLiveData<String>(null)

    // store data of location
    var distance = MutableLiveData<Double>(0.0)
    var speed = MutableLiveData<Double>(0.0)
    var maxSpeed = MutableLiveData<Double>(0.0)

    var distanceRecording = MutableLiveData<Double>(0.0)

    // state of recording
    var recording = MutableLiveData<Boolean>(false)

    // store long lat
    val long = MutableLiveData<Double>(null)
    val lat = MutableLiveData<Double>(null)
    val firstAltitude = MutableLiveData<Double>(0.0)
    val secondAltitude = MutableLiveData<Double>(0.0)

    //data from roomDB
    private val roomDB = RoomDB.getInstance(application)
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)
    
    // store heart rate
    val mBPM = MutableLiveData(0)
    val highmBPM = MutableLiveData(0)
    val lowmBPM = MutableLiveData(300)
    val graph = MutableLiveData(mutableListOf<Entry>())
    val testGraphMulti = MutableLiveData(mutableListOf<Entry>())
    val barGraph = MutableLiveData(mutableListOf<BarEntry>())

    //data to show bar graph
    val steps = MutableLiveData(0.0)
    val cals = MutableLiveData(0.0)
    val hours = MutableLiveData(0.0)

    //get user info
    fun getInfo(): LiveData<UserData> = roomDB.userDao().getAll()
    fun insertUser(userData: UserData) {
        coroutineScope.launch {
            roomDB.userDao().insert(userData)
        }
    }
    fun updateInfo(userData: UserData) {
        coroutineScope.launch {
            roomDB.userDao().update(userData)
        }
    }

    //get walk history
    fun getAllWalks(): LiveData<List<WalkData>> = roomDB.walkDao().getAll()
    fun insertWalk(walkData: WalkData) {
        coroutineScope.launch {
            roomDB.walkDao().insert(walkData)
        }
    }
    fun getWalkById(id: Long): LiveData<List<WalkData>> = roomDB.walkDao().getWalkById(id)

    //get run history
    fun getAllRuns(): LiveData<List<RunData>> = roomDB.runDAO().getAll()
    fun insertRun(runData: RunData) {
        coroutineScope.launch {
            roomDB.runDAO().insert(runData)
        }
    }
    fun getRunById(id: Long): LiveData<List<RunData>> = roomDB.runDAO().getRunById(id)
}