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
interface WalkDAO {
    @Insert
    fun insert(walkData: WalkData)

    @Query("SELECT * FROM walkdata")
    fun getAll(): LiveData<List<WalkData>>

    @Query("SELECT * from walkdata where id == :id")
    fun getWalkById(id: Long): LiveData<List<WalkData>>
}

@Dao
interface RunDAO {
    @Insert
    fun insert(runData: RunData)

    @Query("SELECT * FROM rundata")
    fun getAll(): LiveData<List<RunData>>

    @Query("SELECT * from rundata where id == :id")
    fun getRunById(id: Long): LiveData<List<RunData>>
}
