package com.tanvi.taskflow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tanvi.taskflow.data.repository.TaskRepository

class TasksViewModelFactory (
    private val repository: TaskRepository
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)){
            return TasksViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown viewModel class")
    }
}