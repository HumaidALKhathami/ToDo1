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
    private val toDoIdLiveData = MutableLiveData<UUID>()

    var ToDoLiveData:LiveData<ToDo?> = Transformations.switchMap(toDoIdLiveData){
        toDoRepository.getToDo(it)
    }

}