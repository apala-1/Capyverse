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
import com.example.capyverse.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NoteAdapter(private val notes: MutableList<Note>) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.noteTitle)
        val content: TextView = itemView.findViewById(R.id.noteContent)
        val deleteButton: Button = itemView.findViewById(R.id.deleteNoteButton)
        val editButton: Button = itemView.findViewById(R.id.editNoteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        holder.deleteButton.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("notes")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child(note.id)
                .removeValue()
        }

        holder.editButton.setOnClickListener {
            showEditDialog(holder.itemView.context, note)
        }
    }

    override fun getItemCount(): Int = notes.size

    private fun showEditDialog(context: Context, note: Note) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_note, null)
        val titleEditText = dialogView.findViewById<EditText>(R.id.editNoteTitle)
        val contentEditText = dialogView.findViewById<EditText>(R.id.editNoteContent)

        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        AlertDialog.Builder(context)
            .setTitle("Edit Note")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedNote = Note(
                    note.id,
                    titleEditText.text.toString(),
                    contentEditText.text.toString()
                )

                FirebaseDatabase.getInstance().getReference("notes")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child(note.id)
                    .setValue(updatedNote)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
