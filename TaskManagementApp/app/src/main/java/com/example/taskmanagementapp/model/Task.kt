package com.example.taskmanagementapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val taskname: String,
    val taskdesc: String,
    val taskpriority: String,
    val taskdeadline: String,
): Parcelable
