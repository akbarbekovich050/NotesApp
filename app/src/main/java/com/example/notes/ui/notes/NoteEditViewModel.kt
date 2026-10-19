package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Folder
import com.example.notes.data.Note
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class EditState(
    val loaded: Boolean = false,
    val note: Note? = null,
    val emptyError: Boolean = false,
    val saved: Boolean = false,
)

class NoteEditViewModel(
    private val repo: NotesRepository,
    private val userId: Long,
    private val noteId: Long?,
) : ViewModel() {

    private val _state = MutableStateFlow(EditState())
    val state: StateFlow<EditState> = _state.asStateFlow()

    val folders: StateFlow<List<Folder>> = repo.observeFolders(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            val note = noteId?.let { repo.getNote(it) }
            _state.value = EditState(loaded = true, note = note)
        }
    }

    fun save(title: String, content: String, color: Int, folderId: Long?) {
        if (title.isBlank() && content.isBlank()) {
            _state.value = _state.value.copy(emptyError = true)
            return
        }
        viewModelScope.launch {
            repo.saveNote(userId, noteId, title.trim(), content.trim(), color, folderId)
            _state.value = _state.value.copy(emptyError = false, saved = true)
        }
    }

    fun clearError() {
        if (_state.value.emptyError) _state.value = _state.value.copy(emptyError = false)
    }
}
