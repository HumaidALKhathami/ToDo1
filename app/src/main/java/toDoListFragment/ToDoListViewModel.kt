package toDoListFragment

import androidx.lifecycle.ViewModel
import database.ToDoRepository

class ToDoListViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()

    val liveDataToDo = toDoRepository.getAllToDo()



}