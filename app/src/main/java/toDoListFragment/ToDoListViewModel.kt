package toDoListFragment

import androidx.lifecycle.ViewModel
import com.example.todo.ToDo
import database.ToDoRepository

class ToDoListViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()

    val liveDataToDo = toDoRepository.getAllToDo()

    fun addtoDo (toDo:ToDo){
        toDoRepository.addToDo(toDo)
    }

    fun updateToDo(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

}