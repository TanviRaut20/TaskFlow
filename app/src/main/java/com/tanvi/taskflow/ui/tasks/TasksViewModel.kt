package com.tanvi.taskflow.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanvi.taskflow.data.model.Task
import com.tanvi.taskflow.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: TaskRepository
): ViewModel(){

    private val _selectedFilter = MutableStateFlow(TaskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    val filteredTasks = combine(
        repository.getAllTasks(),
        selectedFilter
    ){ tasks, filter ->

        when(filter){
            TaskFilter.ALL -> tasks

            TaskFilter.PENDING -> {
                tasks.filter { !it.isCompleted }
            }
            TaskFilter.COMPLETED -> {
                tasks.filter { it.isCompleted }
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFilter(filter: TaskFilter){
        _selectedFilter.value = filter
    }

    fun insertTask(task: Task){
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}