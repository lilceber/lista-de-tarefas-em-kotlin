package com.example.listadetarefas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    /** Data da tarefa como LocalDate.toEpochDay() */
    val dateEpochDay: Long,
    /** Horário em minutos desde 00:00 (ex.: 09:30 = 570) */
    val timeMinutes: Int,
    val category: String,
    /** Chave do ícone escolhido pelo usuário (ver TaskIcons) */
    val iconKey: String,
    val done: Boolean = false
)
