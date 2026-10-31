package com.tanvi.taskflow.data.model

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean = false
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}