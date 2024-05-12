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

    fun getTaskList() = taskRepository.getTaskList()

    fun inserTask(task: Task):MutableLiveData<Resource<Long>>{
        return taskRepository.insertTask(task)
    }

    fun deleteTask(task: Task):MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTask(task)
    }

    fun deleteTaskUsingId(taskId: String):MutableLiveData<Resource<Int>>{
        return taskRepository.deleteTaskUsingId(taskId)
    }

    fun updateTask(task: Task):MutableLiveData<Resource<Int>>{
        return taskRepository.updateTask(task)
    }

    fun updateTaskParticularField(taskId: String,title:String,description:String):MutableLiveData<Resource<Int>>{
        return taskRepository.updateTaskParticularField(taskId,title,description)
    }

    fun searchTaskList(query: String){
        return taskRepository.searchTaskList(query)
    }
}