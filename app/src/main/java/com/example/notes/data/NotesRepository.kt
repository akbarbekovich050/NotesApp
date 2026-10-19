package com.example.notes.data

import android.content.ContentValues
import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * Доступ к заметкам и папкам. Все observe-методы возвращают Flow,
 * который заново читает базу после каждого изменения.
 */
class NotesRepository(private val db: NotesDbHelper) {

    private val version = MutableStateFlow(0)

    private fun changed() = version.update { it + 1 }

    // ---------- Заметки ----------

    fun observeNotes(userId: Long, filter: NoteFilter): Flow<List<Note>> =
        version.map { queryNotes(userId, filter) }.flowOn(Dispatchers.IO)

    fun observeNote(noteId: Long): Flow<Note?> =
        version.map { queryNote(noteId) }.flowOn(Dispatchers.IO)

    suspend fun getNote(noteId: Long): Note? = withContext(Dispatchers.IO) { queryNote(noteId) }

    /** Создаёт заметку, если [noteId] == null, иначе обновляет. Возвращает id. */
    suspend fun saveNote(
        userId: Long,
        noteId: Long?,
        title: String,
        content: String,
        color: Int,
        folderId: Long?,
    ): Long = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val values = ContentValues().apply {
            put("title", title)
            put("content", content)
            put("color", color)
            if (folderId == null) putNull("folder_id") else put("folder_id", folderId)
            put("updated_at", now)
        }
        val id = if (noteId == null) {
            values.put("user_id", userId)
            values.put("created_at", now)
            db.writableDatabase.insert("notes", null, values)
        } else {
            db.writableDatabase.update("notes", values, "id = ?", arrayOf(noteId.toString()))
            noteId
        }
        changed()
        id
    }

    suspend fun setFavorite(noteId: Long, favorite: Boolean) = updateFlag(noteId, "is_favorite", favorite)

    suspend fun moveToTrash(noteId: Long) = updateFlag(noteId, "is_deleted", true)

    suspend fun restore(noteId: Long) = updateFlag(noteId, "is_deleted", false)

    suspend fun deleteForever(noteId: Long) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete("notes", "id = ?", arrayOf(noteId.toString()))
        changed()
    }

    suspend fun emptyTrash(userId: Long) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete("notes", "user_id = ? AND is_deleted = 1", arrayOf(userId.toString()))
        changed()
    }

    private suspend fun updateFlag(noteId: Long, column: String, value: Boolean) = withContext(Dispatchers.IO) {
        val values = ContentValues().apply { put(column, if (value) 1 else 0) }
        db.writableDatabase.update("notes", values, "id = ?", arrayOf(noteId.toString()))
        changed()
    }

    private fun queryNotes(userId: Long, filter: NoteFilter): List<Note> {
        val where = StringBuilder("n.user_id = ?")
        val args = mutableListOf(userId.toString())
        when (filter) {
            NoteFilter.All -> where.append(" AND n.is_deleted = 0")
            NoteFilter.Favorites -> where.append(" AND n.is_deleted = 0 AND n.is_favorite = 1")
            NoteFilter.Trash -> where.append(" AND n.is_deleted = 1")
            is NoteFilter.InFolder -> {
                where.append(" AND n.is_deleted = 0 AND n.folder_id = ?")
                args += filter.folderId.toString()
            }
            is NoteFilter.Search -> {
                where.append(" AND n.is_deleted = 0 AND (n.title LIKE ? OR n.content LIKE ?)")
                val pattern = "%${filter.query.trim()}%"
                args += pattern
                args += pattern
            }
        }
        val cursor = db.readableDatabase.rawQuery(
            "$NOTE_SELECT WHERE $where ORDER BY n.updated_at DESC",
            args.toTypedArray(),
        )
        return cursor.use { c -> buildList { while (c.moveToNext()) add(c.toNote()) } }
    }

    private fun queryNote(noteId: Long): Note? {
        val cursor = db.readableDatabase.rawQuery("$NOTE_SELECT WHERE n.id = ?", arrayOf(noteId.toString()))
        return cursor.use { if (it.moveToFirst()) it.toNote() else null }
    }

    private fun Cursor.toNote() = Note(
        id = getLong(0),
        folderId = if (isNull(1)) null else getLong(1),
        folderName = if (isNull(2)) null else getString(2),
        title = getString(3),
        content = getString(4),
        color = getInt(5),
        isFavorite = getInt(6) == 1,
        isDeleted = getInt(7) == 1,
        createdAt = getLong(8),
        updatedAt = getLong(9),
    )

    // ---------- Папки ----------

    fun observeFolders(userId: Long): Flow<List<Folder>> =
        version.map { queryFolders(userId) }.flowOn(Dispatchers.IO)

    suspend fun getFolder(folderId: Long): Folder? = withContext(Dispatchers.IO) {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT id, name FROM folders WHERE id = ?",
            arrayOf(folderId.toString()),
        )
        cursor.use { if (it.moveToFirst()) Folder(it.getLong(0), it.getString(1), 0) else null }
    }

    /** Возвращает false, если папка с таким именем уже существует. */
    suspend fun addFolder(userId: Long, name: String): Boolean = withContext(Dispatchers.IO) {
        val exists = db.readableDatabase.rawQuery(
            "SELECT 1 FROM folders WHERE user_id = ? AND name = ? COLLATE NOCASE",
            arrayOf(userId.toString(), name.trim()),
        ).use { it.moveToFirst() }
        if (exists) return@withContext false
        insertFolder(userId, name.trim())
        changed()
        true
    }

    suspend fun deleteFolder(folderId: Long) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete("folders", "id = ?", arrayOf(folderId.toString()))
        changed()
    }

    private fun insertFolder(userId: Long, name: String): Long {
        val values = ContentValues().apply {
            put("user_id", userId)
            put("name", name)
        }
        return db.writableDatabase.insert("folders", null, values)
    }

    private fun queryFolders(userId: Long): List<Folder> {
        val cursor = db.readableDatabase.rawQuery(
            """
            SELECT f.id, f.name,
                   (SELECT COUNT(*) FROM notes n WHERE n.folder_id = f.id AND n.is_deleted = 0)
            FROM folders f WHERE f.user_id = ? ORDER BY f.name COLLATE NOCASE
            """.trimIndent(),
            arrayOf(userId.toString()),
        )
        return cursor.use { c ->
            buildList { while (c.moveToNext()) add(Folder(c.getLong(0), c.getString(1), c.getInt(2))) }
        }
    }

    // ---------- Статистика ----------

    fun observeStats(userId: Long): Flow<Stats> =
        version.map {
            val uid = userId.toString()
            Stats(
                notes = count("SELECT COUNT(*) FROM notes WHERE user_id = ? AND is_deleted = 0", uid),
                favorites = count("SELECT COUNT(*) FROM notes WHERE user_id = ? AND is_deleted = 0 AND is_favorite = 1", uid),
                folders = count("SELECT COUNT(*) FROM folders WHERE user_id = ?", uid),
            )
        }.flowOn(Dispatchers.IO)

    private fun count(sql: String, arg: String): Int =
        db.readableDatabase.rawQuery(sql, arrayOf(arg)).use { if (it.moveToFirst()) it.getInt(0) else 0 }

    // ---------- Демо-данные для нового пользователя ----------

    fun seedDemoData(userId: Long) {
        val study = insertFolder(userId, "Учёба")
        val personal = insertFolder(userId, "Личное")
        insertFolder(userId, "Идеи")
        val now = System.currentTimeMillis()
        val hour = 60 * 60 * 1000L
        val demo = listOf(
            DemoNote("Добро пожаловать!", "Это ваше приложение для заметок.\n\n• Нажмите «+», чтобы создать заметку\n• Звёздочка добавляет заметку в избранное\n• Удалённые заметки попадают в корзину", 2, null, true),
            DemoNote("Лабораторная работа №2", "Сделать дизайн в Figma (10+ экранов), затем приложение в Android Studio. Не забыть .apk и инструкцию.", 3, study, false),
            DemoNote("Список покупок", "Молоко, хлеб, яблоки, сыр, кофе", 1, personal, false),
            DemoNote("Цитата дня", "Лучший способ начать — перестать говорить и начать делать.", 5, null, true),
        )
        demo.forEachIndexed { index, d ->
            val values = ContentValues().apply {
                put("user_id", userId)
                if (d.folderId == null) putNull("folder_id") else put("folder_id", d.folderId)
                put("title", d.title)
                put("content", d.content)
                put("color", d.color)
                put("is_favorite", if (d.favorite) 1 else 0)
                put("created_at", now - index * hour)
                put("updated_at", now - index * hour)
            }
            db.writableDatabase.insert("notes", null, values)
        }
        changed()
    }

    private data class DemoNote(
        val title: String,
        val content: String,
        val color: Int,
        val folderId: Long?,
        val favorite: Boolean,
    )

    private companion object {
        const val NOTE_SELECT = """
            SELECT n.id, n.folder_id, f.name, n.title, n.content, n.color,
                   n.is_favorite, n.is_deleted, n.created_at, n.updated_at
            FROM notes n LEFT JOIN folders f ON f.id = n.folder_id
        """
    }
}
