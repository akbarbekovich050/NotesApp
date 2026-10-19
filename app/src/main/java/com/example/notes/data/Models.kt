package com.example.notes.data

data class User(
    val id: Long,
    val name: String,
    val email: String,
)

data class Folder(
    val id: Long,
    val name: String,
    val noteCount: Int,
)

data class Note(
    val id: Long,
    val folderId: Long?,
    val folderName: String?,
    val title: String,
    val content: String,
    val color: Int,
    val isFavorite: Boolean,
    val isDeleted: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
)

data class Stats(
    val notes: Int,
    val favorites: Int,
    val folders: Int,
)

/** Какие заметки показывать в списке. */
sealed interface NoteFilter {
    data object All : NoteFilter
    data object Favorites : NoteFilter
    data object Trash : NoteFilter
    data class InFolder(val folderId: Long) : NoteFilter
    data class Search(val query: String) : NoteFilter
}
