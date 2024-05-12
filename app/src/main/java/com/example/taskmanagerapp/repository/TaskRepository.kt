package com.example.taskmanagerapp.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.example.taskmanagerapp.database.TaskDatabase
import com.example.taskmanagerapp.models.Task
import com.example.taskmanagerapp.utils.Resource
import com.example.taskmanagerapp.utils.Resource.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskRepository(application: Application) {

    private val taskDao = TaskDatabase.getInstance(application).taskDao

    private val _taskStateFlow = MutableStateFlow<Resource<Flow<List<Task>>>>(Loading())
    val taskStateFlow: StateFlow<Resource<Flow<List<Task>>>>
        get() = _taskStateFlow

    fun getTaskList() = flow {
        emit(Loading())
        try {
            val result = taskDao.getTaskList()
            emit(Success(result))
        }catch (e:Exception){
            emit(Error(e.message.toString()))
        }
    }

    fun insertTask(task: Task) = MutableLiveData<Resource<Long>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.insertTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun deleteTask(task: Task) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }
    fun deleteTaskUsingId(taskId:String) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTaskUsingId(taskId)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun updateTask(task: Task) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTask(task)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun updateTaskParticularField(taskId: String,title:String,description:String) = MutableLiveData<Resource<Int>>().apply {
        postValue(Loading())
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTaskParticularField(taskId,title,description)
                postValue(Success(result))
            }
        }catch (e:Exception){
            postValue(Error(e.message.toString()))
        }
    }

    fun searchTaskList(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _taskStateFlow.emit(Loading())
                val result = taskDao.searchTaskList("%${query}%")
                _taskStateFlow.emit(Success( result))
            } catch (e: Exception) {
                _taskStateFlow.emit(Error(e.message.toString()))
            }
        }
    }
}