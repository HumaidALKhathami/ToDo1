package toDoListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Query
import com.example.todo.R
import com.example.todo.ToDo
import database.ToDoRepository
import java.util.*
import java.util.concurrent.Executors

val executor = Executors.newSingleThreadExecutor()

class ToDoListViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()

    val liveDataToDo = toDoRepository.getAllToDo()



    fun updateToDo(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    val red = R.color.red


    fun sorting(sortType:String):LiveData<List<ToDo>>  = toDoRepository.sorting(sortType)

    fun updateIsCompleted(isCompleted: Boolean , id: UUID) {
        executor.execute {
            toDoRepository.updateIsCompleted(isCompleted, id)

        }

    }
}