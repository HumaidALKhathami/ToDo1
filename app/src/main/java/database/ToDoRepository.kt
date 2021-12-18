package database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query
import androidx.room.Room
import com.example.todo.ToDo
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "ToDo-database"

class ToDoRepository private constructor(context: Context){

    private val database:ToDoDatabase = Room.databaseBuilder(
        context.applicationContext,
        ToDoDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val toDoDao = database.ToDoDao()

    private val executor = Executors.newSingleThreadExecutor()

    suspend fun getAllToDo(): Flow<List<ToDo>> = toDoDao.getAllToDo()

    fun getToDo(id: UUID):LiveData<ToDo?> = toDoDao.getToDo(id)



    fun sorting(sortType:String):LiveData<List<ToDo>> = toDoDao.sorting(sortType)




    fun addToDo(toDo:ToDo){
        executor.execute{toDoDao.addToDo(toDo)}
    }

    fun deleteToDo(toDo: ToDo){
        executor.execute{
            toDoDao.deleteToDo(toDo)
        }
    }

    fun updateToDo(toDo: ToDo){
        executor.execute{
            toDoDao.updateToDo(toDo)
        }
    }



    companion object {
        var INSTANCE: ToDoRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ToDoRepository(context)
            }
        }


        fun get(): ToDoRepository {
            return INSTANCE ?: throw IllegalArgumentException("ToDoRepository must be initialized")
        }
    }


}