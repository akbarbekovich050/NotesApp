package com.example.notes.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentRegisterBinding
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted

/** Экран 3. Регистрация нового пользователя. */
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel by appViewModels { RegisterViewModel(it.userRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentRegisterBinding.bind(view)

        b.registerButton.setOnClickListener {
            viewModel.register(
                b.name.text.toString(),
                b.email.text.toString(),
                b.password.text.toString(),
                b.repeat.text.toString(),
            )
        }
        b.toLogin.setOnClickListener { findNavController().popBackStack() }

        collectWhileStarted(viewModel.state) { s ->
            b.nameLayout.error = s.nameError?.let(::getString)
            b.emailLayout.error = s.emailError?.let(::getString)
            b.passwordLayout.error = s.passwordError?.let(::getString)
            b.repeatLayout.error = s.repeatError?.let(::getString)
            b.registerButton.isEnabled = !s.loading
            if (s.success) findNavController().navigate(R.id.action_register_to_notes)
        }
    }
}
