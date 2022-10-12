package com.example.terveyshelppi.Service

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.terveyshelppi.Service.RoomDB.UserData

@Composable
fun ShowSensorData(model: ResultViewModel, application: Application) {
    val viewModel = ResultViewModel(application)

    val value by model.stepValue.observeAsState()
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
            data.value!!.avatar
        )
        viewModel.updateInfo(user)
    }
}
