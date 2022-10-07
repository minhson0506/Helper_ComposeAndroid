package com.example.terveyshelppi.Service.YouTubeService

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terveyshelppi.Service.RoomDB.RoomDB
import com.example.terveyshelppi.Service.RoomDB.RunData
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.RoomDB.WalkData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    //data from roomDB
    private val roomDB = RoomDB.getInstance(application)
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Default)

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