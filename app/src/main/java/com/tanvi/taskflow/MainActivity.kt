package com.tanvi.taskflow

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tanvi.taskflow.data.model.Priority
import com.tanvi.taskflow.data.model.Task
import com.tanvi.taskflow.ui.tasks.TasksAdapter
import com.tanvi.taskflow.databinding.ActivityMainBinding
import com.tanvi.taskflow.databinding.BottomSheetAddTaskBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tasksAdapter = TasksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        loadSampleTasks()

        binding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tasksAdapter
        }
    }

    private fun loadSampleTasks() {
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

    private fun showAddTaskBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetAddTaskBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        selectPriority(sheetBinding)
        dialog.show()
    }

    private fun selectPriority(sheetBinding: BottomSheetAddTaskBinding) {
        val items = listOf("Low", "Medium", "High")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
    }
}