package com.example.notes.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.databinding.ItemNoteBinding

class NoteAdapter(
    private val onClick: (Note) -> Unit,
) : ListAdapter<Note, NoteAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    inner class Holder(private val b: ItemNoteBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(note: Note) {
            val ctx = b.root.context
            b.root.setCardBackgroundColor(ContextCompat.getColor(ctx, NoteColors.colorRes(note.color)))
            b.title.text = note.title.ifBlank { ctx.getString(R.string.untitled) }
            b.preview.text = note.content
            b.preview.isVisible = note.content.isNotBlank()
            b.date.text = formatDate(note.updatedAt)
            b.folder.text = note.folderName
            b.folder.isVisible = note.folderName != null
            b.star.isVisible = note.isFavorite
            b.root.setOnClickListener { onClick(note) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(old: Note, new: Note) = old.id == new.id
        override fun areContentsTheSame(old: Note, new: Note) = old == new
    }
}
