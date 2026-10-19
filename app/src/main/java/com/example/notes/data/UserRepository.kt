package com.example.notes.data

import android.content.ContentValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

sealed interface AuthResult {
    data class Success(val userId: Long) : AuthResult
    data object EmailTaken : AuthResult
    data object WrongCredentials : AuthResult
}

class UserRepository(
    private val db: NotesDbHelper,
    private val session: SessionManager,
    private val notes: NotesRepository,
) {

    suspend fun register(name: String, email: String, password: String): AuthResult =
        withContext(Dispatchers.IO) {
            val normalized = email.trim().lowercase()
            if (findUserId(normalized) != null) return@withContext AuthResult.EmailTaken
            val values = ContentValues().apply {
                put("name", name.trim())
                put("email", normalized)
                put("password_hash", hash(password))
            }
            val id = db.writableDatabase.insert("users", null, values)
            notes.seedDemoData(id)
            session.userId = id
            AuthResult.Success(id)
        }

    suspend fun login(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT id FROM users WHERE email = ? AND password_hash = ?",
            arrayOf(email.trim().lowercase(), hash(password)),
        )
        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getLong(0)
                session.userId = id
                AuthResult.Success(id)
            } else {
                AuthResult.WrongCredentials
            }
        }
    }

    suspend fun currentUser(): User? = withContext(Dispatchers.IO) {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT id, name, email FROM users WHERE id = ?",
            arrayOf(session.userId.toString()),
        )
        cursor.use {
            if (it.moveToFirst()) User(it.getLong(0), it.getString(1), it.getString(2)) else null
        }
    }

    fun logout() = session.logout()

    private fun findUserId(email: String): Long? {
        val cursor = db.readableDatabase.rawQuery("SELECT id FROM users WHERE email = ?", arrayOf(email))
        return cursor.use { if (it.moveToFirst()) it.getLong(0) else null }
    }

    private fun hash(password: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
