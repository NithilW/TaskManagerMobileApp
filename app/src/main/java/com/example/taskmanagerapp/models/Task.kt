package com.example.taskmanagerapp.models

import android.icu.text.CaseMap.Title
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity()
data class Task(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "taskId")
    val id:String,
    @ColumnInfo(name = "taskTitle")
    val title: String,
    val description:String,
    val date : Date
)
