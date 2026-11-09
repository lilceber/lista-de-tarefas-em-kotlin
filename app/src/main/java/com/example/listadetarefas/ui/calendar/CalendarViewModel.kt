package com.example.listadetarefas.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.data.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun weekStart(date: LocalDate): LocalDate =
    date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarViewModel(private val repository: TaskRepository) : ViewModel() {
    private val _selected = MutableStateFlow(LocalDate.now())
    val selected: StateFlow<LocalDate> = _selected.asStateFlow()

    /** Tarefas da semana que contém a data selecionada. */
    val weekTasks: StateFlow<List<Task>> = _selected
        .map { weekStart(it) }
        .distinctUntilChanged()
        .flatMapLatest { start ->
            repository.observeRange(start.toEpochDay(), start.toEpochDay() + 6)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun select(date: LocalDate) { _selected.value = date }
    fun shiftWeek(weeks: Long) { _selected.value = _selected.value.plusWeeks(weeks) }
    fun toggle(task: Task) {
        viewModelScope.launch { repository.setDone(task.id, !task.done) }
    }
}
