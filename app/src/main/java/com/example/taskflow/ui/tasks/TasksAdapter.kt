package com.example.taskflow.ui.tasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.data.model.Priority
import com.example.taskflow.data.model.Task
import com.tanvi.taskflow.databinding.ItemTaskBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksAdapter.TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)

        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksAdapter.TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskViewHolder(
        private val binding: ItemTaskBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(task: Task) = with(binding){
            tvTaskTitle.text = task.title
            tvTaskDescription.text = task.description

            tvTaskPriority.text = when(task.priority){
                Priority.LOW -> "LOW PRIORITY"
                Priority.HIGH -> "HIGH PRIORITY"
                Priority.MEDIUM -> "MEDIUM PRIORITY"
            }

            tvTaskPriority.setTextColor(
                when(task.priority){
                    Priority.LOW -> Color.rgb(30, 125, 90)
                    Priority.HIGH -> Color.rgb(211, 55, 55)
                    Priority.MEDIUM -> Color.rgb(185, 119, 0)
                }
            )

            tvTaskTitle.alpha = if (task.isCompleted) 0.5f else 1f
        }
    }

    private class TaskDiffCallBack: DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }


}