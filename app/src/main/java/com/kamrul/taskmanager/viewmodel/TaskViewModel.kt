package com.kamrul.taskmanager.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kamrul.taskmanager.database.TaskDatabase
import com.kamrul.taskmanager.model.Task
import com.kamrul.taskmanager.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application){

    private val _taskId = MutableLiveData<Int>()
    val taskId : LiveData<Int> get() = _taskId

    private var repository: TaskRepository
    var taskList: LiveData<List<Task>?>

    init {
        repository = TaskRepository(TaskDatabase.getDatabase(application).taskDao())

        taskList = repository.readAllTask
    }

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            val generatedId = repository.addTask(task).toInt()
            //id = generatedId
            _taskId.postValue(generatedId)
            Log.d("task_id", "addTask: task_id: $generatedId")
        }
        taskList = repository.readAllTask
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
        taskList = repository.readAllTask
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
        taskList = repository.readAllTask
    }
}