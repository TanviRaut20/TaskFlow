package com.tanvi.taskflow.data.repository

import com.tanvi.taskflow.data.local.TaskDao
import com.tanvi.taskflow.data.local.TaskEntity
import com.tanvi.taskflow.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun Task.toEntity(): TaskEntity{
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        priority = priority,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

fun TaskEntity.toTask(): Task{
    return Task(
        id = id,
        title = title,
        description = description,
        priority = priority,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}

class TaskRepository (
    private val taskDao: TaskDao
){
    //observe all tasks
    fun getAllTasks() : Flow<List<Task>>{
        return taskDao.getAllTasks().map { taskEntities ->
            taskEntities.map { it.toTask() }
        }
    }

    //Insert a task
    suspend fun insertTask(task: Task){
        taskDao.insertTask(task.toEntity())
    }

    //Update a task
    suspend fun updateTask(task: Task){
        taskDao.updateTask(task.toEntity())
    }

    //Delete a task
    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task.toEntity())
    }
}

