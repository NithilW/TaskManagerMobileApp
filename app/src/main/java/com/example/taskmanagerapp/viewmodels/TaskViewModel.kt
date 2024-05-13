package com.example.taskmanagerapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.example.taskmanagerapp.models.Task
import com.example.taskmanagerapp.repository.TaskRepository
import com.example.taskmanagerapp.utils.Resource

class TaskViewModel(application: Application):AndroidViewModel(application) {
    private val taskRepository = TaskRepository(application)
    val taskStateFlow get() =  taskRepository.taskStateFlow
    val statusLiveData get() =  taskRepository.statusLiveData
    val sortByLiveData get() =  taskRepository.sortByLiveData


    fun setSortBy(sort:Pair<String,Boolean>){
        taskRepository.setSortBy(sort)
    }
    fun getTaskList(isAsc : Boolean, sortByName:String) {
        taskRepository.getTaskList(isAsc, sortByName)
    }


    fun insertTask(task: Task){
        taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun deleteTaskUsingId(taskId: String){
        taskRepository.deleteTaskUsingId(taskId)
    }

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun searchTaskList(query: String){
        taskRepository.searchTaskList(query)
    }
}