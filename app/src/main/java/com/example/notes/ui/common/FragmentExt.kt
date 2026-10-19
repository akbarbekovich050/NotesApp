package com.example.notes.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.notes.NotesApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

val Fragment.app: NotesApp get() = requireActivity().application as NotesApp

/** Создаёт ViewModel с доступом к зависимостям приложения. */
inline fun <reified VM : ViewModel> Fragment.appViewModels(noinline create: (NotesApp) -> VM) =
    viewModels<VM> { viewModelFactory { initializer { create(app) } } }

/** Подписка на Flow, пока экран видим. */
fun <T> Fragment.collectWhileStarted(flow: Flow<T>, action: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { action(it) }
        }
    }
}
