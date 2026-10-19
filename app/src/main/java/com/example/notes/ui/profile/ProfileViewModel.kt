package com.example.notes.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.NotesApp
import com.example.notes.data.NotesRepository
import com.example.notes.data.SessionManager
import com.example.notes.data.Stats
import com.example.notes.data.User
import com.example.notes.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val users: UserRepository,
    notes: NotesRepository,
    private val session: SessionManager,
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    val stats: StateFlow<Stats> = notes.observeStats(session.userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Stats(0, 0, 0))

    val darkTheme: Boolean get() = session.darkTheme

    init {
        viewModelScope.launch { _user.value = users.currentUser() }
    }

    fun setDarkTheme(enabled: Boolean) {
        if (session.darkTheme == enabled) return
        session.darkTheme = enabled
        NotesApp.applyTheme(enabled)
    }

    fun logout() = users.logout()
}
