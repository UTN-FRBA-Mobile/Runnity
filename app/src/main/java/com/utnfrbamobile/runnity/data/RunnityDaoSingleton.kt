package com.utnfrbamobile.runnity.data

import android.content.Context
import androidx.room.Room

object RunnityDaoSingleton {

    private var runnityDao: RunnityDao? = null

    fun getInstance(context: Context): RunnityDao {
        if (runnityDao == null) {
            runnityDao = Room
                .databaseBuilder(context, RunnityDataBase::class.java, "runnity")
                .build().runnutyDao()
        }
        return runnityDao!!
    }
}