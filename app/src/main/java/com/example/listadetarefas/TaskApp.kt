package com.example.listadetarefas

import android.app.Application
import com.example.listadetarefas.data.AppDatabase
import com.example.listadetarefas.data.TaskRepository

class TaskApp : Application() {
    val repository: TaskRepository by lazy {
        TaskRepository(AppDatabase.get(this).taskDao())
    }
}
