package com.example.terveyshelppi.Service.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    @PrimaryKey
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
    val totalHours: Int

) {
}