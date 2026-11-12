package com.tanvi.taskflow

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tanvi.taskflow.data.local.TaskDatabaseProvider
import com.tanvi.taskflow.data.model.Priority
import com.tanvi.taskflow.data.model.Task
import com.tanvi.taskflow.data.repository.TaskRepository
import com.tanvi.taskflow.ui.tasks.TasksAdapter
import com.tanvi.taskflow.databinding.ActivityMainBinding
import com.tanvi.taskflow.databinding.BottomSheetAddTaskBinding
import com.tanvi.taskflow.ui.tasks.TasksViewModel
import com.tanvi.taskflow.ui.tasks.TasksViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.tanvi.taskflow.ui.tasks.TaskFilter
import com.tanvi.taskflow.ui.tasks.TaskSortOption

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var viewModel: TasksViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = TaskDatabaseProvider.getDatabase(applicationContext)
        val repository = TaskRepository(database.taskDao())
        val factory = TasksViewModelFactory(repository)

        viewModel = ViewModelProvider(this,factory)[TasksViewModel::class.java]


        tasksAdapter = TasksAdapter(onTaskChecked = { task, isChecked ->
            val updatedTask = task.copy(isCompleted = isChecked)
            viewModel.updateTask(updatedTask)
        },
            onTaskDelete = { task ->
                MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Task?")
                    .setMessage("Are you sure you want to delete '${task.title}'")
                    .setNegativeButton("Cancel"){ dialog, _ ->
                        dialog.dismiss()
                    }.setPositiveButton("Delete"){ _,_ ->
                        viewModel.deleteTask(task)
                    }.show()

            }
        )

        setUpRecyclerView()
        observeTasks()
        setupTaskFilters()
        setUpSearch()

        binding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()
        }

        binding.btnSortTasks.setOnClickListener {
            showSortDialog()
        }
    }

    private fun showSortDialog(){
        val sortingOptions = arrayOf(
            "Newest first",
            "Priority (High → Low)",
            "Alphabetical (A–Z)")

        val selectedPosition = when (viewModel.selectedSortOption.value) {
            TaskSortOption.NEWEST_FIRST -> 0
            TaskSortOption.PRIORITY -> 1
            TaskSortOption.ALPHABETICAL -> 2
        }

        MaterialAlertDialogBuilder(this).setTitle("Sort tasks")
            .setSingleChoiceItems(sortingOptions, selectedPosition){ dialog, which ->
                val selectedSortOption = when (which) {
                    0 -> TaskSortOption.NEWEST_FIRST
                    1 -> TaskSortOption.PRIORITY
                    2 -> TaskSortOption.ALPHABETICAL
                    else -> TaskSortOption.NEWEST_FIRST
                }
                viewModel.sortTasks(selectedSortOption)
                dialog.dismiss()
        }.setNegativeButton("Cancel", null)
            .show()

    }

    private fun setupTaskFilters(){
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkIds ->
            val selectedFilter = when (checkIds.firstOrNull()){
                R.id.chipAll -> TaskFilter.ALL
                R.id.chipPending -> TaskFilter.PENDING
                R.id.chipCompleted -> TaskFilter.COMPLETED

                else -> TaskFilter.ALL
            }
            viewModel.setFilter(selectedFilter)
        }
    }

    private fun setUpRecyclerView() {
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tasksAdapter
        }
    }

    private fun setUpSearch(){
        binding.etSearchTasks.doAfterTextChanged { editable ->
            viewModel.updateSearchQuery(editable.toString())
        }
    }

    private fun showAddTaskBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetAddTaskBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        selectPriority(sheetBinding)
        dialog.show()

        sheetBinding.btnSaveTask.setOnClickListener {
            val title = sheetBinding.etTaskTitle.text.toString().trim()

            if (title.isEmpty()) {
                sheetBinding.tilTaskTitle.error = "Task title is required"
                return@setOnClickListener
            }

            sheetBinding.tilTaskTitle.error = null

            val description = sheetBinding.etTaskDescription.text.toString().trim()
            val selectedPriority = sheetBinding.actTaskPriority.text.toString()

            if (selectedPriority.isEmpty()) {
                sheetBinding.tilTaskPriority.error = "Priority is required"
                return@setOnClickListener
            }
            sheetBinding.tilTaskPriority.error = null

            val taskCreated = Task(
                java.util.UUID.randomUUID().toString(),
                title,
                description,
                Priority.valueOf(selectedPriority.uppercase())
            )

            viewModel.insertTask(taskCreated)
            dialog.dismiss()

        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.rvTasks.visibility =
            if (isEmpty) View.GONE else View.VISIBLE

        binding.layoutEmptyState.visibility =
            if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun selectPriority(sheetBinding: BottomSheetAddTaskBinding) {
        val items = listOf("Low", "Medium", "High")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        sheetBinding.actTaskPriority.setAdapter(adapter)
    }

    private fun observeTasks(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.filteredTasks.collect { tasks ->
                    tasksAdapter.submitList(tasks)
                    updateEmptyState(tasks.isEmpty())
                }
            }
        }
    }


}