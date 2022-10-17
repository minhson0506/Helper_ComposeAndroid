package com.example.terveyshelppi.service

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.terveyshelppi.service.roomDB.RoomDB
import com.example.terveyshelppi.service.roomDB.UserData
import com.example.terveyshelppi.service.roomDB.ExerciseData
import com.example.terveyshelppi.service.youTubeService.SearchResponse
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.github.mikephil.charting.data.Entry
import com.google.android.libraries.maps.model.LatLng

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    // store data of searching
    val result = MutableLiveData<List<SearchResponse.Item>>(null)

    // data from step sensor
    private val _stepValue: MutableLiveData<String> = MutableLiveData()
    val stepValue: LiveData<String> = _stepValue
    fun updateStepValue(value: String) {
        _stepValue.value = value
    }

    // data from temperature sensor
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
    var distance = MutableLiveData(0.0)
    var speed = MutableLiveData(0.0)
    val locationState = MutableLiveData(false)
    // points for GG map
    val points = MutableLiveData(mutableListOf<LatLng>())

    // state of exercising
    var recording = MutableLiveData(false)
    var distanceRecording = MutableLiveData(0.0)

    // store long lat
    val long = MutableLiveData<Double>(null)
    val lat = MutableLiveData<Double>(null)
    val firstAltitude = MutableLiveData(0.0)
    val secondAltitude = MutableLiveData(0.0)

    // store heart rate
    val mBPM = MutableLiveData(0)
    val highmBPM = MutableLiveData(0)
    val lowmBPM = MutableLiveData(300)
    val graph = MutableLiveData(mutableListOf<Entry>())
    val testGraphMulti = MutableLiveData(mutableListOf<Entry>())
    val barGraph = MutableLiveData(mutableListOf<BarEntry>())
    val listBPM = MutableLiveData(mutableListOf<Int>())

    //data to show bar graph
    val hours = MutableLiveData(0.0)

    // state of update profile
    val state = MutableLiveData(true)

    //data from roomDB
    private val roomDB = RoomDB.getInstance(application)
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

    //get and set user info
    fun getInfo(): LiveData<UserData> = roomDB.userDao().getAll()
    fun insertUser(userData: UserData) {
        coroutineScope.launch {
            roomDB.userDao().insert(userData)
        }
    }

    fun updateInfo(userData: UserData) {
        state.postValue(false)
        coroutineScope.launch {
            roomDB.userDao().update(userData)
            state.postValue(true)
        }
    }

    //get and set exercise history
    fun getAllExercises(): LiveData<List<ExerciseData>> = roomDB.exerciseDao().getAll()
    fun insertExercise(exerciseData: ExerciseData) {
        coroutineScope.launch {
            roomDB.exerciseDao().insert(exerciseData)
        }
    }
}