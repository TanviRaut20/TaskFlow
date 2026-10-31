package com.tanvi.taskflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanvi.taskflow.data.model.Priority
import com.tanvi.taskflow.data.model.Task
import com.tanvi.taskflow.ui.tasks.TasksAdapter
import com.tanvi.taskflow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tasksAdapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        loadSampleTasks()
    }

    private fun setUpRecyclerView(){
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tasksAdapter
        }
    }

    private fun loadSampleTasks(){
        val tasks = listOf(
            Task(
                id = "1",
                title = "Learn Kotlin Coroutines",
                description = "Understand suspend functions and Flow",
                priority = Priority.HIGH
            ),
            Task(
                id = "2",
                title = "Build TaskFlow UI",
                description = "Create reusable XML task cards",
                priority = Priority.MEDIUM
            ),
            Task(
                id = "3",
                title = "Read Firebase documentation",
                description = "Explore Authentication and Firestore",
                priority = Priority.LOW
            )
        )

        tasksAdapter.submitList(tasks)
    }
}