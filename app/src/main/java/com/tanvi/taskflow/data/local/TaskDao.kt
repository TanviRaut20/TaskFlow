package com.tanvi.taskflow.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Insert
    suspend fun insertTask(taskEntity: TaskEntity)

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)

    @Update
    suspend fun updateTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks ORDER BY rowid DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
}