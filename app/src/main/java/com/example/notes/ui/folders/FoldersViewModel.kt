package com.example.notes.ui.folders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.R
import com.example.notes.data.Folder
import com.example.notes.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoldersViewModel(
    private val repo: NotesRepository,
    private val userId: Long,
) : ViewModel() {

    val folders: StateFlow<List<Folder>?> = repo.observeFolders(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Возвращает id строки с ошибкой в [onResult] или null при успехе. */
    fun addFolder(name: String, onResult: (Int?) -> Unit) {
        if (name.isBlank()) {
            onResult(R.string.error_folder_empty)
            return
        }
        viewModelScope.launch {
            val added = repo.addFolder(userId, name)
            onResult(if (added) null else R.string.error_folder_exists)
        }
    }

    fun deleteFolder(folder: Folder) {
        viewModelScope.launch { repo.deleteFolder(folder.id) }
    }
}
