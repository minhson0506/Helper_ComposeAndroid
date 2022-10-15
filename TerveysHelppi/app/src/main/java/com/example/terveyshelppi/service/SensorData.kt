package com.example.terveyshelppi.service

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.terveyshelppi.service.roomDB.UserData

@Composable
fun ShowSensorData(model: ResultViewModel, application: Application) {
    val viewModel = ResultViewModel(application)

    val value by model.stepValue.observeAsState()
    val data by viewModel.getInfo().observeAsState()

    if (value != null && data != null) {
        val user = UserData(
            data!!.name,
            data!!.weight,
            data!!.height,
            data!!.targetSteps,
            data!!.targetCals,
            data!!.targetHours,
            data!!.heartRate,
            data!!.totalDistance,
            data!!.totalCalories,
            value!!.toDouble(),
            data!!.totalHours,
            data!!.avatar,
            data!!.stepBeginOfDay,
            data!!.day
        )
        viewModel.updateInfo(user)
    }
}
