package com.utnfrbamobile.runnity.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RunnityDao {

    @Query("SELECT * from LocationEntity")
    fun getAll(): List<LocationEntity>

    @Query("SELECT * from LocationEntity")
    fun getAllLiveData(): LiveData<List<LocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locations: LocationEntity)
}