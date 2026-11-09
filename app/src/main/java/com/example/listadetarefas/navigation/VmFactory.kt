package com.example.listadetarefas.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.listadetarefas.data.TaskRepository
import com.example.listadetarefas.ui.calendar.CalendarViewModel
import com.example.listadetarefas.ui.edit.EditTaskViewModel
import com.example.listadetarefas.ui.home.HomeViewModel

class VmFactory(
    private val repository: TaskRepository,
    private val taskId: Long = -1L,
    private val day: Long = 0L
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository)
        modelClass.isAssignableFrom(CalendarViewModel::class.java) -> CalendarViewModel(repository)
        modelClass.isAssignableFrom(EditTaskViewModel::class.java) ->
            EditTaskViewModel(repository, taskId, day)
        else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    } as T
}
