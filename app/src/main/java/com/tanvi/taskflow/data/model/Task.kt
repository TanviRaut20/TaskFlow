package com.tanvi.taskflow.data.model

data class Task(
    var id: String,
    var title: String,
    var description: String,
    var priority: Priority,
    var isCompleted: Boolean = false
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}