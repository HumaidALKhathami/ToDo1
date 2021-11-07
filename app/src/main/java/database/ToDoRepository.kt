package database

import android.content.Context
import androidx.room.Room
import java.lang.IllegalArgumentException

private const val DATABASE_NAME = "ToDo-database"

class ToDoRepository private constructor(context: Context){

    private val database:ToDoDatabase = Room.databaseBuilder(
        context.applicationContext,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val ToDoDao = database.ToDoDao()

    companion object{
        var INSTANCE:ToDoRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = ToDoRepository(context)
            }
        }
    }

    fun get() : ToDoRepository{
        return INSTANCE ?: throw IllegalArgumentException("ToDoRepository must be initialized")
    }
}