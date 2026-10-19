package com.example.notes.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.NoteFilter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Экран 4. Все заметки (главный экран). */
class NotesFragment : BaseNoteListFragment() {
    override val filter = NoteFilter.All
    override val emptyTitle = R.string.empty_notes_title
    override val emptyText = R.string.empty_notes_text

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_notes, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                if (item.itemId != R.id.action_search) return false
                findNavController().navigate(R.id.searchFragment)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}

/** Экран 10. Избранные заметки. */
class FavoritesFragment : BaseNoteListFragment() {
    override val filter = NoteFilter.Favorites
    override val emptyTitle = R.string.empty_favorites_title
    override val emptyText = R.string.empty_favorites_text
    override val emptyIcon = R.drawable.ic_star_outline
    override val showFab = false
}

/** Экран 11. Корзина: удалённые заметки можно восстановить или стереть. */
class TrashFragment : BaseNoteListFragment() {
    override val filter = NoteFilter.Trash
    override val emptyTitle = R.string.empty_trash_title
    override val emptyText = R.string.empty_trash_text
    override val emptyIcon = R.drawable.ic_delete
    override val showFab = false

    private var hasNotes = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_trash, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                menu.findItem(R.id.action_empty_trash)?.isVisible = hasNotes
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                if (item.itemId != R.id.action_empty_trash) return false
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.action_empty_trash)
                    .setMessage(R.string.confirm_empty_trash)
                    .setNegativeButton(R.string.action_cancel, null)
                    .setPositiveButton(R.string.action_delete) { _, _ -> viewModel.emptyTrash() }
                    .show()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onNotesShown(count: Int) {
        hasNotes = count > 0
        requireActivity().invalidateOptionsMenu()
    }
}

/** Экран 9. Заметки внутри одной папки. */
class FolderNotesFragment : BaseNoteListFragment() {
    private val folderId: Long get() = requireArguments().getLong(ARG_FOLDER_ID)

    override val filter get() = NoteFilter.InFolder(folderId)
    override val emptyTitle = R.string.empty_folder_title
    override val emptyText = R.string.empty_folder_text
    override val emptyIcon = R.drawable.ic_folder
    override val folderForNewNote get() = folderId
}
