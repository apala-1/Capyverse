package com.example.capyverse.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capyverse.R
import com.example.capyverse.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TaskAdapter(private val tasks: MutableList<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.taskTitle)
        val description: TextView = itemView.findViewById(R.id.taskDescription)
        val dueDate: TextView = itemView.findViewById(R.id.dueDate)
        val deleteButton: Button = itemView.findViewById(R.id.deleteTaskButton)
        val editButton: Button = itemView.findViewById(R.id.editTaskButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.dueDate.text = task.dueDate

        holder.deleteButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("tasks")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(task.id)
                .removeValue()
        }

        holder.editButton.setOnClickListener {
            showEditDialog(holder.itemView.context, task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    private fun showEditDialog(context: Context, task: Task) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_task, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editTaskTitle)
        val descriptionEditText = dialogView.findViewById<EditText>(R.id.editTaskDescription)
        val dueDateEditText = dialogView.findViewById<EditText>(R.id.editDueDate)

        titleEditText.setText(task.title)
        descriptionEditText.setText(task.description)
        dueDateEditText.setText(task.dueDate)

        AlertDialog.Builder(context)
            .setTitle("Edit Task")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedTask = Task(
                    task.id,
                    titleEditText.text.toString(),
                    descriptionEditText.text.toString(),
                    dueDateEditText.text.toString()
                )

                FirebaseDatabase.getInstance().getReference("tasks")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child(task.id)
                    .setValue(updatedTask)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
