package toDoFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.ToDo
import database.ToDoRepository
import java.util.*

class ToDoFragmentViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()
    val toDoIdLiveData = MutableLiveData<UUID>()

    var toDoLiveData:LiveData<ToDo?> = Transformations.switchMap(toDoIdLiveData){
        toDoRepository.getToDo(it)
    }

    fun loadToDo(toDoId:UUID){
        toDoIdLiveData.value = toDoId
    }

    fun updateToDo(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    fun deleteToDo(toDo: ToDo){
        toDoRepository.deleteToDo(toDo)
    }

    fun addToDo (toDo:ToDo){
        toDoRepository.addToDo(toDo)
    }
}