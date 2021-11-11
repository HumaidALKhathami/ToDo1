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

    @Query("SELECT * FROM ToDo ORDER BY CASE WHEN :sortType = 'title' THEN title END, CASE WHEN :sortType = 'creationDate' THEN creationDate END, CASE WHEN :sortType = 'dueDate' THEN dueDate END , CASE WHEN :sortType = '0' THEN isCompleted = 0 END, CASE WHEN :sortType = '1' THEN isCompleted = 1 END")
    fun sorting(sortType:String):LiveData<List<ToDo>>

    @Query("UPDATE ToDo SET isCompleted = :isCompleted WHERE id = :id")
    fun updateIsCompleted(isCompleted: Boolean , id: UUID)
}