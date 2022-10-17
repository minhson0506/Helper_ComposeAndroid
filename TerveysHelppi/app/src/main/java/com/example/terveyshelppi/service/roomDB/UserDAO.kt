package com.example.terveyshelppi.service.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userData: UserData)

    @Update
    fun update(userData: UserData)

    @Query("SELECT * FROM userdata")
    fun getAll(): LiveData<UserData>
}

@Dao
interface ExerciseDAO {
    @Insert
    fun insert(exerciseData: ExerciseData)

    @Query("SELECT * FROM exercisedata")
    fun getAll(): LiveData<List<ExerciseData>>

}

