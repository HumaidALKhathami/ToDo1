package com.example.todo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class ToDo (@PrimaryKey
    val id : UUID = UUID.randomUUID(),
                 var title : String = "",
                 var description : String = "",
                 var isCompleted :Boolean = false,
                 val creationDate: Date = Date(),
                 var dueDate: Date = Date()
    )
