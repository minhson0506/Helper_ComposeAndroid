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
    val userData by viewModel.getInfo().observeAsState()

    // update steps from sensor to Room
    if (value != null && userData != null) {
        val user = UserData(
            userData!!.name,
            userData!!.weight,
            userData!!.height,
            userData!!.targetSteps,
            userData!!.targetCals,
            userData!!.targetHours,
            userData!!.heartRate,
            userData!!.totalDistance,
            userData!!.totalCalories,
            value!!.toDouble(),
            userData!!.totalHours,
            userData!!.avatar,
            userData!!.stepBeginOfDay,
            userData!!.day
        )
        viewModel.updateInfo(user)
    }
}
