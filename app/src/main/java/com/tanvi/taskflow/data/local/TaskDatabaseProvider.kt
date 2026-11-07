package com.tanvi.taskflow.data.local

import android.content.Context
import androidx.room.Room

object TaskDatabaseProvider {
    private var INSTANCE: TaskDatabase? = null

    fun getDatabase(context: Context): TaskDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                TaskDatabase::class.java,
                "taskflow_database").build()

            INSTANCE = instance

            instance
        }
    }
}