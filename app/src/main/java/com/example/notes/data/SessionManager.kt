package com.example.notes.data

import android.content.Context
import androidx.core.content.edit

/** Настройки и текущая сессия хранятся в SharedPreferences и переживают перезапуск. */
class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, NO_USER)
        set(value) = prefs.edit { putLong(KEY_USER_ID, value) }

    val isLoggedIn: Boolean get() = userId != NO_USER

    var darkTheme: Boolean
        get() = prefs.getBoolean(KEY_DARK, false)
        set(value) = prefs.edit { putBoolean(KEY_DARK, value) }

    fun logout() {
        userId = NO_USER
    }

    companion object {
        const val NO_USER = -1L
        private const val KEY_USER_ID = "user_id"
        private const val KEY_DARK = "dark_theme"
    }
}
