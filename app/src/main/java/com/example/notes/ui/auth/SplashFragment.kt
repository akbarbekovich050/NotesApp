package com.example.notes.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.ui.common.app
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Экран 1. Заставка: решает, куда вести пользователя. */
class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1200)
            val target = if (app.session.isLoggedIn) {
                R.id.action_splash_to_notes
            } else {
                R.id.action_splash_to_login
            }
            findNavController().navigate(target)
        }
    }
}
