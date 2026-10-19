package com.example.notes.ui.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.data.Folder
import com.example.notes.databinding.DialogFolderBinding
import com.example.notes.databinding.FragmentNoteListBinding
import com.example.notes.databinding.ItemFolderBinding
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted
import com.example.notes.ui.notes.BaseNoteListFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Экран 8. Список папок: создание, удаление, переход в папку. */
class FoldersFragment : Fragment(R.layout.fragment_note_list) {

    private val viewModel by appViewModels { FoldersViewModel(it.notesRepository, it.session.userId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentNoteListBinding.bind(view)
        val adapter = FolderAdapter(
            onClick = { folder ->
                findNavController().navigate(
                    R.id.folderNotesFragment,
                    bundleOf(
                        BaseNoteListFragment.ARG_FOLDER_ID to folder.id,
                        BaseNoteListFragment.ARG_FOLDER_NAME to folder.name,
                    ),
                )
            },
            onLongClick = ::confirmDelete,
        )
        b.list.adapter = adapter
        b.empty.icon.setImageResource(R.drawable.ic_folder)
        b.empty.emptyTitle.setText(R.string.empty_folders_title)
        b.empty.emptyText.setText(R.string.empty_folders_text)
        b.fab.contentDescription = getString(R.string.new_folder)
        b.fab.setOnClickListener { showAddDialog() }

        var hintShown = false
        collectWhileStarted(viewModel.folders) { folders ->
            if (folders == null) return@collectWhileStarted
            adapter.submitList(folders)
            b.empty.root.isVisible = folders.isEmpty()
            b.list.isVisible = folders.isNotEmpty()
            if (folders.isNotEmpty() && !hintShown && savedInstanceState == null) {
                hintShown = true
                Toast.makeText(requireContext(), R.string.folder_hint_long_press, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAddDialog() {
        val db = DialogFolderBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.new_folder)
            .setView(db.root)
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_add, null)
            .show()
        db.name.doAfterTextChanged { db.nameLayout.error = null }
        // Своя обработка кнопки, чтобы диалог не закрывался при ошибке.
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            viewModel.addFolder(db.name.text.toString()) { error ->
                if (error == null) dialog.dismiss() else db.nameLayout.error = getString(error)
            }
        }
    }

    private fun confirmDelete(folder: Folder) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.action_delete)
            .setMessage(getString(R.string.confirm_delete_folder, folder.name))
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> viewModel.deleteFolder(folder) }
            .show()
    }
}

/** Русские формы слова независимо от языка устройства: 1 заметка, 2 заметки, 5 заметок. */
private fun notesCount(n: Int): String {
    val word = when {
        n % 10 == 1 && n % 100 != 11 -> "заметка"
        n % 10 in 2..4 && n % 100 !in 12..14 -> "заметки"
        else -> "заметок"
    }
    return "$n $word"
}

private class FolderAdapter(
    private val onClick: (Folder) -> Unit,
    private val onLongClick: (Folder) -> Unit,
) : ListAdapter<Folder, FolderAdapter.Holder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    inner class Holder(private val b: ItemFolderBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(folder: Folder) {
            b.name.text = folder.name
            b.count.text = notesCount(folder.noteCount)
            b.root.setOnClickListener { onClick(folder) }
            b.root.setOnLongClickListener {
                onLongClick(folder)
                true
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Folder>() {
        override fun areItemsTheSame(old: Folder, new: Folder) = old.id == new.id
        override fun areContentsTheSame(old: Folder, new: Folder) = old == new
    }
}
