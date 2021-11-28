package com.utnfrbamobile.runnity.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [LocationEntity::class],
    version = 1
)
abstract class RunnityDataBase : RoomDatabase() {

    abstract fun runnutyDao(): RunnityDao
}