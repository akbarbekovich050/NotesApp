package com.example.notes.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentProfileBinding
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Экран 12. Профиль и настройки. */
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by appViewModels {
        ProfileViewModel(it.userRepository, it.notesRepository, it.session)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentProfileBinding.bind(view)

        b.statNotes.label.setText(R.string.stat_notes)
        b.statFavorites.label.setText(R.string.stat_favorites)
        b.statFolders.label.setText(R.string.stat_folders)

        b.darkTheme.isChecked = viewModel.darkTheme
        b.darkTheme.setOnCheckedChangeListener { _, checked -> viewModel.setDarkTheme(checked) }
        b.trash.setOnClickListener { findNavController().navigate(R.id.trashFragment) }
        b.about.setOnClickListener { findNavController().navigate(R.id.aboutFragment) }
        b.logout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.confirm_logout)
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.action_logout) { _, _ ->
                    viewModel.logout()
                    findNavController().navigate(R.id.action_global_login)
                }
                .show()
        }

        collectWhileStarted(viewModel.user) { user ->
            if (user == null) return@collectWhileStarted
            b.name.text = user.name
            b.email.text = user.email
            b.avatar.text = user.name.trim().take(1).uppercase()
        }
        collectWhileStarted(viewModel.stats) { s ->
            b.statNotes.value.text = s.notes.toString()
            b.statFavorites.value.text = s.favorites.toString()
            b.statFolders.value.text = s.folders.toString()
        }
    }
}
