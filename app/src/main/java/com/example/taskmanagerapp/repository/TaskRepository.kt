package com.example.taskmanagerapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanagerapp.database.TaskDatabase
import com.example.taskmanagerapp.models.Task
import com.example.taskmanagerapp.utils.Resource
import com.example.taskmanagerapp.utils.Resource.*
import com.example.taskmanagerapp.utils.StatusResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


class TaskRepository(application: Application) {

    private val taskDao = TaskDatabase.getInstance(application).taskDao

    private val _taskStateFlow = MutableStateFlow<Resource<Flow<List<Task>>>>(Loading())
    val taskStateFlow: StateFlow<Resource<Flow<List<Task>>>>
        get() = _taskStateFlow

    private val _statusLiveData = MutableLiveData<Resource<StatusResult>>()
    val statusLiveData: LiveData<Resource<StatusResult>>
        get() = _statusLiveData

    fun getTaskList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _taskStateFlow.emit(Loading())
                delay(500)
                val result = taskDao.getTaskList()
                _taskStateFlow.emit(Success("loading", result))
            } catch (e: Exception) {
                _taskStateFlow.emit(Error(e.message.toString()))
            }
        }
    }

    fun insertTask(task: Task) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.insertTask(task)
                handleResult(result.toInt(), "Task Added", StatusResult.Added)
            }
        } catch (e: Exception) {
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun deleteTask(task: Task) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTask(task)
                handleResult(result, "Task Deleted", StatusResult.Deleted)

            }
        } catch (e: Exception) {
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }
    fun deleteTaskUsingId(taskId: String) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.deleteTaskUsingId(taskId)
                handleResult(result, "Task Deleted", StatusResult.Deleted)

            }
        } catch (e: Exception) {
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun updateTask(task: Task) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = taskDao.updateTask(task)
                handleResult(result, "Task Updated", StatusResult.Updated)

            }
        } catch (e: Exception) {
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }


    fun searchTaskList(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _taskStateFlow.emit(Loading())
                val result = taskDao.searchTaskList("%${query}%")
                _taskStateFlow.emit(Success("loading", result))
            } catch (e: Exception) {
                _taskStateFlow.emit(Error(e.message.toString()))
            }
        }
    }
    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {
        if (result != -1) {
            _statusLiveData.postValue(Success(message, statusResult))
        } else {
            _statusLiveData.postValue(Error("Something Went Wrong", statusResult))
        }
    }
}