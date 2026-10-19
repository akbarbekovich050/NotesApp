package com.example.notes.ui.auth

import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.R
import com.example.notes.data.AuthResult
import com.example.notes.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Состояние форм входа/регистрации: ошибки полей хранятся как id строк. */
data class AuthState(
    @StringRes val nameError: Int? = null,
    @StringRes val emailError: Int? = null,
    @StringRes val passwordError: Int? = null,
    @StringRes val repeatError: Int? = null,
    @StringRes val formError: Int? = null,
    val loading: Boolean = false,
    val success: Boolean = false,
)

private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

class LoginViewModel(private val users: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        val emailError = if (isValidEmail(email)) null else R.string.error_email_invalid
        val passwordError = if (password.length >= 6) null else R.string.error_password_short
        _state.value = AuthState(emailError = emailError, passwordError = passwordError)
        if (emailError != null || passwordError != null) return

        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            _state.value = when (users.login(email, password)) {
                is AuthResult.Success -> AuthState(success = true)
                else -> AuthState(formError = R.string.error_wrong_credentials)
            }
        }
    }
}

class RegisterViewModel(private val users: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun register(name: String, email: String, password: String, repeat: String) {
        val errors = AuthState(
            nameError = if (name.isNotBlank()) null else R.string.error_name_empty,
            emailError = if (isValidEmail(email)) null else R.string.error_email_invalid,
            passwordError = if (password.length >= 6) null else R.string.error_password_short,
            repeatError = if (password == repeat) null else R.string.error_password_mismatch,
        )
        _state.value = errors
        if (listOf(errors.nameError, errors.emailError, errors.passwordError, errors.repeatError).any { it != null }) {
            return
        }

        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            _state.value = when (users.register(name, email, password)) {
                is AuthResult.Success -> AuthState(success = true)
                AuthResult.EmailTaken -> AuthState(emailError = R.string.error_email_taken)
                AuthResult.WrongCredentials -> AuthState()
            }
        }
    }
}
