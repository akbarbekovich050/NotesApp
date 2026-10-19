package com.example.notes.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.Note
import com.example.notes.databinding.FragmentNoteDetailBinding
import com.example.notes.ui.common.NoteColors
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted
import com.example.notes.ui.common.formatDate
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Экран 6. Просмотр заметки: избранное, редактирование, удаление. */
class NoteDetailFragment : Fragment(R.layout.fragment_note_detail) {

    private val noteId: Long get() = requireArguments().getLong(BaseNoteListFragment.ARG_NOTE_ID)

    private val viewModel by appViewModels { NoteDetailViewModel(it.notesRepository, noteId) }

    private var current: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNoteDetailBinding.bind(view)

        b.restoreButton.setOnClickListener {
            viewModel.restore { toastAndClose(R.string.restored) }
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_note_detail, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                val note = current
                val inTrash = note?.isDeleted == true
                menu.findItem(R.id.action_favorite)?.apply {
                    isVisible = note != null && !inTrash
                    setIcon(if (note?.isFavorite == true) R.drawable.ic_star else R.drawable.ic_star_outline)
                    setTitle(if (note?.isFavorite == true) R.string.action_unfavorite else R.string.action_favorite)
                }
                menu.findItem(R.id.action_edit)?.isVisible = note != null && !inTrash
                menu.findItem(R.id.action_delete)?.isVisible = note != null && !inTrash
                menu.findItem(R.id.action_delete_forever)?.isVisible = inTrash
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_favorite -> viewModel.toggleFavorite()
                    R.id.action_edit -> findNavController().navigate(
                        R.id.noteEditFragment,
                        bundleOf(BaseNoteListFragment.ARG_NOTE_ID to noteId),
                    )
                    R.id.action_delete -> confirm(R.string.confirm_delete_text) {
                        viewModel.moveToTrash { toastAndClose(R.string.moved_to_trash) }
                    }
                    R.id.action_delete_forever -> confirm(R.string.confirm_delete_forever_text) {
                        viewModel.deleteForever { findNavController().popBackStack() }
                    }
                    else -> return false
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        collectWhileStarted(viewModel.note) { note ->
            current = note
            requireActivity().invalidateOptionsMenu()
            if (note == null) return@collectWhileStarted
            b.card.setCardBackgroundColor(ContextCompat.getColor(requireContext(), NoteColors.colorRes(note.color)))
            b.title.text = note.title.ifBlank { getString(R.string.untitled) }
            b.content.text = note.content
            b.date.text = formatDate(note.updatedAt)
            b.folder.text = note.folderName
            b.folder.isVisible = note.folderName != null
            b.restoreButton.isVisible = note.isDeleted
        }
    }

    private fun confirm(message: Int, action: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm_delete_title)
            .setMessage(message)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> action() }
            .show()
    }

    private fun toastAndClose(message: Int) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}
