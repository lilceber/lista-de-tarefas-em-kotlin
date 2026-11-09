package com.example.listadetarefas.ui.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listadetarefas.data.Task
import com.example.listadetarefas.data.TaskRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditTaskViewModel(
    private val repository: TaskRepository,
    private val taskId: Long,
    initialDay: Long
) : ViewModel() {

    val isEditing = taskId > 0

    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var dateEpochDay by mutableLongStateOf(
        if (initialDay > 0) initialDay else LocalDate.now().toEpochDay()
    )
    var timeMinutes by mutableIntStateOf(9 * 60)
    var category by mutableStateOf("Pessoal")
    var iconKey by mutableStateOf("star")
    var showTitleError by mutableStateOf(false)

    private var done = false
    private var loaded: Task? = null

    init {
        if (isEditing) {
            viewModelScope.launch {
                repository.getById(taskId)?.let { t ->
                    loaded = t
                    title = t.title
                    description = t.description
                    dateEpochDay = t.dateEpochDay
                    timeMinutes = t.timeMinutes
                    category = t.category
                    iconKey = t.iconKey
                    done = t.done
                }
            }
        }
    }

    fun save(onSaved: () -> Unit) {
        if (title.isBlank()) {
            showTitleError = true
            return
        }
        viewModelScope.launch {
            repository.save(
                Task(
                    id = if (isEditing) taskId else 0,
                    title = title.trim(),
                    description = description.trim(),
                    dateEpochDay = dateEpochDay,
                    timeMinutes = timeMinutes,
                    category = category,
                    iconKey = iconKey,
                    done = done
                )
            )
            onSaved()
        }
    }

    fun delete(onDeleted: () -> Unit) {
        val task = loaded ?: return
        viewModelScope.launch {
            repository.delete(task)
            onDeleted()
        }
    }
}
