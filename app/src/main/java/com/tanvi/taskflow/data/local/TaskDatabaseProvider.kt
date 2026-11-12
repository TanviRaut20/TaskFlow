package com.tanvi.taskflow.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
object TaskDatabaseProvider {
    private var INSTANCE: TaskDatabase? = null

    fun getDatabase(context: Context): TaskDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                TaskDatabase::class.java,
                "taskflow_database").addMigrations(MIGRATION_1_2).build()

            INSTANCE = instance

            instance
        }
    }

    private val MIGRATION_1_2 = object : Migration(1,2){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "ALTER TABLE  tasks ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0"
            )
        }
    }
}