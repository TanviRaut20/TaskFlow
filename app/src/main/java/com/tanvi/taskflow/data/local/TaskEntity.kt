package com.tanvi.taskflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tanvi.taskflow.data.model.Priority

@Entity(tableName = "tasks")

data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)