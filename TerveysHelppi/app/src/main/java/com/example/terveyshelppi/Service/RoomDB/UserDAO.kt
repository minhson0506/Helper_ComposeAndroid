package com.example.terveyshelppi.Service.RoomDB

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

//    @Query("SELECT * from walkdata where id == :id")
//    fun getExerciseById(id: Long): LiveData<List<ExerciseData>>
}

