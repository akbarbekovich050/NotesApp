package com.example.notes

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.notes.data.NotesDbHelper
import com.example.notes.data.NotesRepository
import com.example.notes.data.SessionManager
import com.example.notes.data.UserRepository

/** Простой контейнер зависимостей на всё приложение. */
class NotesApp : Application() {

    lateinit var session: SessionManager
        private set
    lateinit var notesRepository: NotesRepository
        private set
    lateinit var userRepository: UserRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = NotesDbHelper(this)
        session = SessionManager(this)
        notesRepository = NotesRepository(db)
        userRepository = UserRepository(db, session, notesRepository)
        applyTheme(session.darkTheme)
    }

    companion object {
        fun applyTheme(dark: Boolean) {
            AppCompatDelegate.setDefaultNightMode(
                if (dark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
