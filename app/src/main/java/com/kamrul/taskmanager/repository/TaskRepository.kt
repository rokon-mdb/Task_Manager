package com.kamrul.taskmanager.repository

import androidx.lifecycle.LiveData
import com.kamrul.taskmanager.database.TaskDao
import com.kamrul.taskmanager.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val readAllTask: LiveData<List<Task>?> = taskDao.readAllTask()

    suspend fun addTask(task: Task): Long{
        return taskDao.addTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

}