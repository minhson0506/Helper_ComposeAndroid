package com.example.terveyshelppi.Service.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RunData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timeStart: Int,
    val distance: Int,
    val duration: Int,
    val averageSpeed: Int,
    val calories: Int,
    val elevation: Int, //m
    val heartRate: Int,
    val pace: Int, //inch/km
    val cadence: Int, //SPM
) {
}