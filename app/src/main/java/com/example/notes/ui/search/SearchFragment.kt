package com.example.notes.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentSearchBinding
import com.example.notes.ui.common.NoteAdapter
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted
import com.example.notes.ui.notes.BaseNoteListFragment

/** Экран 7. Поиск по заголовку и тексту заметок. */
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel by appViewModels { SearchViewModel(it.notesRepository, it.session.userId) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSearchBinding.bind(view)
        val adapter = NoteAdapter { note ->
            findNavController().navigate(
                R.id.noteDetailFragment,
                bundleOf(BaseNoteListFragment.ARG_NOTE_ID to note.id),
            )
        }
        b.list.adapter = adapter
        b.empty.icon.setImageResource(R.drawable.ic_search)

        b.query.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }
        if (savedInstanceState == null) {
            b.query.requestFocus()
            b.query.post {
                requireContext().getSystemService<InputMethodManager>()
                    ?.showSoftInput(b.query, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        collectWhileStarted(viewModel.state) { state ->
            when (state) {
                SearchState.Idle -> {
                    adapter.submitList(emptyList())
                    b.empty.root.isVisible = true
                    b.empty.emptyTitle.setText(R.string.search_hint_title)
                    b.empty.emptyText.setText(R.string.search_hint_text)
                }
                is SearchState.Results -> {
                    adapter.submitList(state.notes)
                    b.empty.root.isVisible = state.notes.isEmpty()
                    b.empty.emptyTitle.setText(R.string.search_empty_title)
                    b.empty.emptyText.setText(R.string.search_empty_text)
                }
            }
        }
    }
}
