package com.example.listadetarefas.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val dao: TaskDao) {
    fun observeByDay(day: Long): Flow<List<Task>> = dao.observeByDay(day)
    fun observeRange(start: Long, end: Long): Flow<List<Task>> = dao.observeRange(start, end)
    suspend fun getById(id: Long): Task? = dao.getById(id)
    suspend fun save(task: Task): Long = dao.upsert(task)
    suspend fun delete(task: Task) = dao.delete(task)
    suspend fun setDone(id: Long, done: Boolean) = dao.setDone(id, done)
}
