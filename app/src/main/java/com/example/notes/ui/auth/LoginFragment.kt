package com.example.notes.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentLoginBinding
import com.example.notes.ui.common.appViewModels
import com.example.notes.ui.common.collectWhileStarted

/** Экран 2. Вход в аккаунт. */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by appViewModels { LoginViewModel(it.userRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentLoginBinding.bind(view)

        b.loginButton.setOnClickListener {
            viewModel.login(b.email.text.toString(), b.password.text.toString())
        }
        b.toRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        collectWhileStarted(viewModel.state) { s ->
            b.emailLayout.error = s.emailError?.let(::getString)
            b.passwordLayout.error = s.passwordError?.let(::getString)
            b.error.isVisible = s.formError != null
            s.formError?.let { b.error.setText(it) }
            b.loginButton.isEnabled = !s.loading
            if (s.success) findNavController().navigate(R.id.action_login_to_notes)
        }
    }
}
