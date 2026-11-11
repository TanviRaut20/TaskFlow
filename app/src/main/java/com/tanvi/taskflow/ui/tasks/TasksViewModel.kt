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
    private val _searchQuery = MutableStateFlow("")
    private val _selectedSortOption = MutableStateFlow(TaskSortOption.NEWEST_FIRST)
    val searchQuery = _searchQuery.asStateFlow()
    val selectedFilter = _selectedFilter.asStateFlow()
    val selectedSortOption = _selectedSortOption.asStateFlow()

    val filteredTasks = combine(
        repository.getAllTasks(),
        selectedFilter,
        searchQuery,
        selectedSortOption
    ){ tasks, filter, query, sortOption ->

        val statusFilteredTasks = when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.PENDING -> tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }

        val searchFilteredTasks = statusFilteredTasks.filter {
            it.title.contains(query, ignoreCase = true)
        }

        val sortedTasks = when (sortOption) {
            TaskSortOption.NEWEST_FIRST -> {
                searchFilteredTasks.sortedByDescending { it.createdAt }
            }

            TaskSortOption.PRIORITY -> {
                searchFilteredTasks.sortedByDescending { it.priority.ordinal }
            }

            TaskSortOption.ALPHABETICAL -> {
                searchFilteredTasks.sortedBy { it.title.lowercase() }
            }
        }

        sortedTasks

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFilter(filter: TaskFilter){
        _selectedFilter.value = filter
    }

    fun sortTasks(sort: TaskSortOption){
        _selectedSortOption.value = sort
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

    fun updateSearchQuery(query: String){
        _searchQuery.value = query
    }
}