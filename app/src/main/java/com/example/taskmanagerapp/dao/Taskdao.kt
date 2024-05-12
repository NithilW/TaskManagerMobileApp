package com.example.taskmanagerapp.dao

import androidx.room.*
import com.example.taskmanagerapp.models.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface Taskdao {

    @Query("SElECT * FROM Task ORDER BY date DESC")
    fun getTaskList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task:Task):Long

    @Delete
    suspend fun  deleteTask(task: Task):Int

    @Query("DELETE FROM Task WHERE  taskId == :taskId")
    suspend fun deleteTaskUsingId(taskId:String) : Int

    @Update
    suspend fun updateTask(task: Task):Int

    @Query("UPDATE Task SET taskTitle=:title,description=:description WHERE taskId =:taskId")
    suspend fun updateTaskParticularField(taskId: String,title:String,description:String):Int

    @Query("SElECT * FROM Task  WHERE taskTitle LIKE:query ORDER BY date DESC")
    fun searchTaskList(query: String): Flow<List<Task>>
}