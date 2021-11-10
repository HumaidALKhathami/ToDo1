package database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.ToDo
import java.util.*

@Dao
interface ToDoDao {


    @Query("SELECT * FROM ToDo")
    fun getAllToDo():LiveData<List<ToDo>>

    @Query("SELECT * FROM ToDo WHERE id = (:id)")
    fun getToDo(id:UUID):LiveData<ToDo?>

    @Insert
    fun addToDo(toDo:ToDo)

    @Delete
    fun deleteToDo(toDo: ToDo)

    @Update
    fun updateToDo(toDo: ToDo)

    @Query("SELECT * FROM ToDo ORDER BY (:sortType)")
    fun sorting(sortType:String):LiveData<List<ToDo>>

}