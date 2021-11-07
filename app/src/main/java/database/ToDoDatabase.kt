package database

import androidx.room.Database
import androidx.room.RoomDatabase

import androidx.room.TypeConverters
import com.example.todo.ToDo

@Database(entities = [ToDo::class] , version = 1)
@TypeConverters(ToDoTypeConverters::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun ToDoDao(): ToDoDao
}