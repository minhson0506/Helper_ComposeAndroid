package com.example.terveyshelppi.Service.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class WalkData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timeStart: String,
    val distance: Int,
    val duration: Int,
    val averageSpeed: Int,
    val calories: Int,
    val elevation: Int, //m
    val heartRate: Int,
    val pace: Int //inch/km
) {
}
