package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class ToDo ( @PrimaryKey
    val id : UUID = UUID.randomUUID(),
    val title : String = "",
    val description : String = "",
    val isCompleted :Boolean = false,
    val creationDate: Date = Date(),
    val dueDate: Date = Date()
    )
