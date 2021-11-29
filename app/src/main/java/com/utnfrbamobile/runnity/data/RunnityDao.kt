package com.utnfrbamobile.runnity.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunnityDao {

    @Query("SELECT * from LocationEntity")
    fun getAll(): List<LocationEntity>

    @Query("SELECT * from LocationEntity")
    fun getAllLiveData(): LiveData<List<LocationEntity>>

    @Query("DELETE from LocationEntity")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locations: LocationEntity)
}