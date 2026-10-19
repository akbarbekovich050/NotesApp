package com.example.notes.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.Note
import com.example.notes.data.NoteFilter
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface SearchState {
    /** Запрос ещё не введён. */
    data object Idle : SearchState
    data class Results(val notes: List<Note>) : SearchState
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(repo: NotesRepository, userId: Long) : ViewModel() {

    private val query = MutableStateFlow("")

    val state: StateFlow<SearchState> = query
        .debounce(250)
        .flatMapLatest { q ->
            if (q.isBlank()) {
                flowOf(SearchState.Idle)
            } else {
                repo.observeNotes(userId, NoteFilter.Search(q)).map { SearchState.Results(it) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState.Idle)

    fun onQueryChanged(text: String) {
        query.value = text
    }
}
