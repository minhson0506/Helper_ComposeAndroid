package com.example.terveyshelppi.Service

import android.app.Application
import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.terveyshelppi.Service.RoomDB.RoomDB
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.RoomDB.ExerciseData
import com.example.terveyshelppi.Service.YouTubeService.SearchResponse
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry
import java.util.*
import kotlin.math.log

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "terveyshelppi"

    // store data of searching
    val result = MutableLiveData<List<SearchResponse.Item>>(null)

    //data from step sensor
    private val _stepValue: MutableLiveData<String> = MutableLiveData()
    val stepValue: LiveData<String> = _stepValue
    fun updateStepValue(value: String) {
        _stepValue.value = value
    }

    private val _tempValue: MutableLiveData<String> = MutableLiveData()
    val tempValue: LiveData<String> = _tempValue
    fun updateTempValue(value: String) {
        _tempValue.value = value
    }

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
    val locationState = MutableLiveData(false)

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
    val listBPM = MutableLiveData(mutableListOf<Int>())

    // state of update profile
    val state = MutableLiveData(true)

    //data to show bar graph
    val steps = MutableLiveData(0.0)
    val cals = MutableLiveData(0.0)
    val hours = MutableLiveData(0.0)

    //get user info
    fun getInfo(): LiveData<UserData> = roomDB.userDao().getAll()
    fun insertUser(userData: UserData) {
        state.postValue(false)
        coroutineScope.launch {
            roomDB.userDao().insert(userData)
            state.postValue(true)
        }
    }

    fun updateInfo(userData: UserData) {
        coroutineScope.launch {
            roomDB.userDao().update(userData)
        }
    }

    //get exercise history
    fun getAllExercises(): LiveData<List<ExerciseData>> = roomDB.exerciseDao().getAll()
    fun insertExercise(exerciseData: ExerciseData) {
        coroutineScope.launch {
            roomDB.exerciseDao().insert(exerciseData)
        }
    }
}