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
import com.example.capyverse.adapter.NoteAdapter
import com.example.capyverse.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotesActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: MutableList<Note>
    private lateinit var adapter: NoteAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return

        database = FirebaseDatabase.getInstance().getReference("notes").child(userId)

        val noteTitle = findViewById<EditText>(R.id.noteTitle)
        val noteContent = findViewById<EditText>(R.id.noteContent)
        val addNoteButton = findViewById<Button>(R.id.addNoteButton)
        recyclerView = findViewById(R.id.noteRecyclerView)

        noteList = mutableListOf()
        adapter = NoteAdapter(noteList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addNoteButton.setOnClickListener {
            val id = database.push().key ?: return@setOnClickListener
            val note = Note(id, noteTitle.text.toString(), noteContent.text.toString())

            database.child(id).setValue(note).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Note Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show()
                }
            }
        }

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    if (note != null) {
                        noteList.add(note)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to Load Notes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
