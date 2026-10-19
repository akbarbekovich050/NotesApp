package com.example.notes.ui.notes

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.data.Folder
import com.example.notes.databinding.FragmentNoteEditBinding
import com.example.notes.ui.common.NoteColors
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted
import com.google.android.material.chip.Chip

/** Экран 5. Создание и редактирование заметки. */
class NoteEditFragment : Fragment(R.layout.fragment_note_edit) {

    private val noteId: Long?
        get() = arguments?.getLong(BaseNoteListFragment.ARG_NOTE_ID, -1L)?.takeIf { it > 0 }

    private val viewModel by appViewModels {
        NoteEditViewModel(it.notesRepository, it.session.userId, noteId)
    }

    private lateinit var b: FragmentNoteEditBinding
    private var folders: List<Folder> = emptyList()
    private var selectedFolderId: Long? = null
    private var filled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b = FragmentNoteEditBinding.bind(view)
        filled = savedInstanceState != null
        (requireActivity() as AppCompatActivity).supportActionBar?.setTitle(
            if (noteId == null) R.string.title_new_note else R.string.title_edit_note
        )

        setupColorChips()
        selectedFolderId = arguments?.getLong(BaseNoteListFragment.ARG_FOLDER_ID, -1L)?.takeIf { it > 0 }

        b.title.doAfterTextChanged { viewModel.clearError() }
        b.content.doAfterTextChanged { viewModel.clearError() }
        b.folder.setOnItemClickListener { _, _, position, _ ->
            selectedFolderId = if (position == 0) null else folders[position - 1].id
        }
        b.saveButton.setOnClickListener {
            viewModel.save(b.title.text.toString(), b.content.text.toString(), selectedColor(), selectedFolderId)
        }

        collectWhileStarted(viewModel.folders) { list ->
            folders = list
            val names = listOf(getString(R.string.no_folder)) + list.map { it.name }
            b.folder.setSimpleItems(names.toTypedArray())
            showSelectedFolder()
        }

        collectWhileStarted(viewModel.state) { s ->
            if (s.loaded && !filled) {
                filled = true
                s.note?.let { note ->
                    b.title.setText(note.title)
                    b.content.setText(note.content)
                    selectedFolderId = note.folderId
                    (b.colors.getChildAt(note.color) as? Chip)?.isChecked = true
                    showSelectedFolder()
                }
            }
            b.titleLayout.error = if (s.emptyError) getString(R.string.error_note_empty) else null
            if (s.saved) {
                Toast.makeText(requireContext(), R.string.note_saved, Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun showSelectedFolder() {
        val name = folders.firstOrNull { it.id == selectedFolderId }?.name ?: getString(R.string.no_folder)
        b.folder.setText(name, false)
    }

    private fun setupColorChips() {
        NoteColors.options.forEachIndexed { index, option ->
            val chip = Chip(requireContext()).apply {
                id = View.generateViewId()
                text = option.label
                isCheckable = true
                isCheckedIconVisible = true
                chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, option.color))
                chipStrokeWidth = resources.displayMetrics.density
                chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.outline))
                isChecked = index == 0
            }
            b.colors.addView(chip)
        }
    }

    private fun selectedColor(): Int {
        for (i in 0 until b.colors.childCount) {
            if ((b.colors.getChildAt(i) as Chip).isChecked) return i
        }
        return 0
    }
}
