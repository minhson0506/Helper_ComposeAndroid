package com.example.terveyshelppi.Service.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserData(
    val name: String,
    val weight: Int,
    val height: Int,
    val targetSteps: Int,
    val targetCals: Int,
    val targetHours: Int,
    val heartRate: Int,
    val totalDistance: Int,
    val totalCalories: Int,
    val totalSteps: Double,
    val totalHours: Int,
    val avatar: String,
    val stepBeginOfDay: Double,
    val day: Int,
    @PrimaryKey
    val id: Int = 0
) {
}