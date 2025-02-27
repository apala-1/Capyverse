package com.example.capyverse.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capyverse.R
import com.example.capyverse.adapter.TaskAdapter
import com.example.capyverse.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskList: MutableList<Task>
    private lateinit var adapter: TaskAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return

        database = FirebaseDatabase.getInstance().getReference("tasks").child(userId)

        val taskTitle = findViewById<EditText>(R.id.taskTitle)
        val taskDescription = findViewById<EditText>(R.id.taskDescription)
        val dueDate = findViewById<EditText>(R.id.dueDate)
        val addTaskButton = findViewById<Button>(R.id.addTaskButton)
        recyclerView = findViewById(R.id.taskRecyclerView)

        taskList = mutableListOf()
        adapter = TaskAdapter(taskList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addTaskButton.setOnClickListener {
            val id = database.push().key ?: return@setOnClickListener
            val task = Task(id, taskTitle.text.toString(), taskDescription.text.toString(), dueDate.text.toString())

            database.child(id).setValue(task).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show()
                }
            }
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                for (taskSnapshot in snapshot.children) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        taskList.add(task)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to Load Tasks", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
