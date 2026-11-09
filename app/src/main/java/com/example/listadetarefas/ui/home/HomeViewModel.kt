package com.example.listadetarefas.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(private val repository: TaskRepository) : ViewModel() {
    private val today = LocalDate.now().toEpochDay()

    val tasks: StateFlow<List<Task>> = repository.observeByDay(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggle(task: Task) {
        viewModelScope.launch { repository.setDone(task.id, !task.done) }
    }
}
