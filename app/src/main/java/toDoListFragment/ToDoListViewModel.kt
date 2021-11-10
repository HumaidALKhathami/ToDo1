package toDoListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Query
import com.example.todo.R
import com.example.todo.ToDo
import database.ToDoRepository

class ToDoListViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()

    val liveDataToDo = toDoRepository.getAllToDo()


    fun updateToDo(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    val red = R.color.red


    fun sorting(sortType:String):LiveData<List<ToDo>>  = toDoRepository.sorting(sortType)



}