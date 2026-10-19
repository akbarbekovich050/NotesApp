package com.example.notes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteFilter
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** null — данные ещё загружаются, пустой список — показываем «пустое состояние». */
class NoteListViewModel(
    private val repo: NotesRepository,
    private val userId: Long,
    filter: NoteFilter,
) : ViewModel() {

    val notes: StateFlow<List<Note>?> = repo.observeNotes(userId, filter)
        .map<List<Note>, List<Note>?> { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun emptyTrash() {
        viewModelScope.launch { repo.emptyTrash(userId) }
    }
}
