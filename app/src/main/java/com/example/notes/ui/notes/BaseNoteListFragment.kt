package com.example.notes.ui.notes

import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.NoteFilter
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.ui.common.NoteAdapter
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted

/** Общая логика экранов-списков заметок. */
abstract class BaseNoteListFragment : Fragment(R.layout.fragment_note_list) {

    protected abstract val filter: NoteFilter
    @get:StringRes protected abstract val emptyTitle: Int
    @get:StringRes protected abstract val emptyText: Int
    @get:DrawableRes protected open val emptyIcon: Int = R.drawable.ic_note
    protected open val showFab: Boolean = true
    /** Папка, которую подставить в новую заметку. */
    protected open val folderForNewNote: Long? = null

    protected val viewModel by appViewModels {
        NoteListViewModel(it.notesRepository, it.session.userId, filter)
    }

    protected lateinit var binding: FragmentNoteListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentNoteListBinding.bind(view)
        val adapter = NoteAdapter { note ->
            findNavController().navigate(R.id.noteDetailFragment, bundleOf(ARG_NOTE_ID to note.id))
        }
        binding.list.adapter = adapter

        binding.empty.emptyTitle.setText(emptyTitle)
        binding.empty.emptyText.setText(emptyText)
        binding.empty.icon.setImageResource(emptyIcon)

        binding.fab.isVisible = showFab
        binding.fab.setOnClickListener {
            findNavController().navigate(
                R.id.noteEditFragment,
                bundleOf(ARG_FOLDER_ID to (folderForNewNote ?: -1L)),
            )
        }

        collectWhileStarted(viewModel.notes) { notes ->
            if (notes == null) return@collectWhileStarted
            adapter.submitList(notes)
            binding.empty.root.isVisible = notes.isEmpty()
            binding.list.isVisible = notes.isNotEmpty()
            onNotesShown(notes.size)
        }
    }

    protected open fun onNotesShown(count: Int) = Unit

    companion object {
        const val ARG_NOTE_ID = "noteId"
        const val ARG_FOLDER_ID = "folderId"
        const val ARG_FOLDER_NAME = "folderName"
    }
}
