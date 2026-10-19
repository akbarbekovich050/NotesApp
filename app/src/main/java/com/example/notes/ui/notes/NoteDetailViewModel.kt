package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val repo: NotesRepository,
    private val noteId: Long,
) : ViewModel() {

    val note: StateFlow<Note?> = repo.observeNote(noteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun toggleFavorite() {
        val current = note.value ?: return
        viewModelScope.launch { repo.setFavorite(noteId, !current.isFavorite) }
    }

    fun moveToTrash(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.moveToTrash(noteId)
            onDone()
        }
    }

    fun restore(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.restore(noteId)
            onDone()
        }
    }

    fun deleteForever(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.deleteForever(noteId)
            onDone()
        }
    }
}
