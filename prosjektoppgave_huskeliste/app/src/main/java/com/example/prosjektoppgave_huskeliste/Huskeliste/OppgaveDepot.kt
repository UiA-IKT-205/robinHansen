package com.example.prosjektoppgave_huskeliste.Huskeliste

import android.content.Context
import com.example.prosjektoppgave_huskeliste.Huskeliste.Lokaldata.Tasks

class TaskDepositoryManager {

    private lateinit var tasksCollection: MutableList<Tasks>

    var onTasks: ((List<Tasks>) -> Unit)? = null
    var onTasksUpdate: ((tasks: Tasks) -> Unit)? = null

    fun deleteDoneTasks() {
        tasksCollection.removeAll { tasksCollection ->
            tasksCollection.isChecked
        }
        onTasks?.invoke(tasksCollection)
    }

    fun loadTasks(context: Context) {
        tasksCollection = mutableListOf()
        onTasks?.invoke(tasksCollection)
    }

    fun updateTask(tasks: Tasks) {
        onTasksUpdate?.invoke(tasks)
    }

    fun addTask(task: Tasks) {
        tasksCollection.add(task)
        onTasks?.invoke(tasksCollection)
    }

    companion object {
        val instance = TaskDepositoryManager()
    }

}