package com.tugasin.app.data

import com.tugasin.app.model.Priority
import com.tugasin.app.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TaskRepository {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private var nextId = 1

    fun addTask(task: Task) {
        val newTask = task.copy(id = nextId++)
        _tasks.value = _tasks.value + newTask
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) task else it
        }
    }

    fun deleteTask(taskId: Int) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    fun toggleTaskCompletion(taskId: Int) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun getTaskById(taskId: Int): Task? {
        return _tasks.value.find { it.id == taskId }
    }

    fun getFilteredAndSortedTasks(
        filterPriority: Priority? = null,
        filterCompleted: Boolean? = null,
        sortByPriority: Boolean = false
    ): List<Task> {
        var result = _tasks.value

        filterPriority?.let { priority ->
            result = result.filter { it.priority == priority }
        }

        filterCompleted?.let { completed ->
            result = result.filter { it.isCompleted == completed }
        }

        if (sortByPriority) {
            result = result.sortedByDescending { it.priority.ordinal }
        }

        return result
    }
}