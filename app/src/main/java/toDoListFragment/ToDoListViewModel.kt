package toDoListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.example.todo.R
import com.example.todo.ToDo
import database.ToDoRepository
import kotlinx.coroutines.flow.Flow


class ToDoListViewModel : ViewModel() {

    private val toDoRepository = ToDoRepository.get()

    suspend fun liveDataToDo():Flow<List<ToDo>> = toDoRepository.getAllToDo()



    fun updateToDo(toDo: ToDo){
        toDoRepository.updateToDo(toDo)
    }

    val red = R.color.red
    val black = R.color.black


    fun sorting(sortType:String):LiveData<List<ToDo>>  = toDoRepository.sorting(sortType)


}