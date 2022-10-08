package com.example.terveyshelppi.Service.Sensors

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.terveyshelppi.Service.RoomDB.UserData
import com.example.terveyshelppi.Service.YouTubeService.ResultViewModel

class SensorViewModel : ViewModel() {
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
}

@Composable
fun ShowSensorData(sensorViewModel: SensorViewModel, application: Application) {
    val TAG = "terveyshelppi"
    val viewModel = ResultViewModel(application)

    val value by sensorViewModel.stepValue.observeAsState()
    val data = viewModel.getInfo().observeAsState()

    if (value != null && data.value != null) {
        val user = UserData(
            data.value!!.name,
            data.value!!.weight,
            data.value!!.height,
            data.value!!.targetSteps,
            data.value!!.targetCals,
            data.value!!.targetHours,
            data.value!!.heartRate,
            data.value!!.totalDistance,
            data.value!!.totalCalories,
            value!!.toDouble(),
            data.value!!.totalHours,
        )
        viewModel.updateInfo(user)
    }
}
