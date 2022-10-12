package com.example.terveyshelppi.Service.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(UserData::class), (WalkData::class), (RunData::class)], version = 4)
abstract class RoomDB : RoomDatabase() {

    abstract fun userDao(): UserDAO
    abstract fun walkDao(): WalkDAO
    abstract fun runDAO(): RunDAO

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomDB::class.java,
                        "dogs.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}