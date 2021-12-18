package toDoListFragment

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.TextView
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import toDoFragment.ToDoFragment
import java.util.*

const val KEY_ID  = "ToDoID"
const val dateFormat = "dd/MMM/yyyy"

class ToDoListFragment : Fragment() {

    private val fragmentListViewModel by lazy { ViewModelProvider(this).get(ToDoListViewModel::class.java) }


    private lateinit var toDoRv : RecyclerView
    private lateinit var addToDo: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_list_menu,menu)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do_list, container, false)

        val linearLayoutManager = LinearLayoutManager(context)

        toDoRv = view.findViewById(R.id.to_do_recycler_view)

        toDoRv.layoutManager = linearLayoutManager

        addToDo = view.findViewById(R.id.add_to_do)

        addToDo()

        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        getAll()



//        fragmentListViewModel.liveDataToDo.observe(
//            viewLifecycleOwner, Observer {
//                updateUI(it)
//            }
//        )



        Log.d("hi" , "from view created")
    }




    private fun updateUI(todos: List<ToDo>){
        val toDoAdapter = ToDoAdapter(todos)


        toDoRv.adapter = toDoAdapter
    }

    private inner class ToDoHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener{

        private val toDoTitle: TextView = itemView.findViewById(R.id.title_item)
        private val toDoCreationDate: TextView = itemView.findViewById(R.id.creation_date_item)
        private val toDoDueDate: TextView = itemView.findViewById(R.id.due_date_item)
        private val isCompletedCb: CheckBox = itemView.findViewById(R.id.checkBox_item)

        private lateinit var toDo: ToDo

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v == itemView){

                val args = Bundle()

                val fragment = ToDoFragment()

                args.putSerializable(KEY_ID,toDo.id)
                fragment.arguments = args

                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }


        }


        fun bind(toDo: ToDo){
            this.toDo = toDo

            val creationDateString = android.text.format.DateFormat.format(dateFormat,toDo.creationDate)

          //  checkIsCompleted()

            toDoTitle.text = toDo.title
            toDoCreationDate.text = creationDateString
            toDoDueDate.text = "enter due date"
            isCompletedCb.isChecked = toDo.isCompleted
            if(toDo.dueDate != null) {
                toDoDueDate.text = android.text.format.DateFormat.format(dateFormat, toDo.dueDate)
            }


            isCompletedCb.setOnCheckedChangeListener{ _, isChecked ->


                toDo.isCompleted = isChecked

                //checkIsCompleted()

                fragmentListViewModel.updateToDo(toDo)


            }


        }


    }

    private inner class ToDoAdapter(var ToDos:List<ToDo>) : RecyclerView.Adapter<ToDoHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {

            val view = layoutInflater.inflate(R.layout.fragment_to_do_item,parent,false)

            return ToDoHolder(view)
        }

        override fun onBindViewHolder(holder: ToDoHolder, position: Int) {

            val toDo = ToDos[position]

            holder.bind(toDo)
        }

        override fun getItemCount(): Int {

            return ToDos.size
        }

    }

    private fun addToDo(){
        addToDo.setOnClickListener {


            val fragment = ToDoFragment()


            activity?.let {
                it.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {



        return when(item.itemId){

            R.id.by_due_date -> {

                removeObserver()
                observer("dueDate")

            true
            }
            R.id.by_creation_date -> {

                removeObserver()
                observer("creationDate")
                true
            }
            R.id.by_alphabetically ->{

                removeObserver()
                observer("title")
                true
            }
            R.id.by_completed -> {

                removeObserver()
                observer("isCompleted0")
                true
            }
            R.id.by_not_completed -> {
                removeObserver()
                observer("isCompleted1")

                true
            }
            else -> return super.onOptionsItemSelected(item)

        }
    }

    private fun observer (sortType:String){


        val liveData = fragmentListViewModel.sorting(sortType)


       liveData.observeOnce(
            viewLifecycleOwner, Observer {
                updateUI(it)
            }
        )



    }

    private fun getAll(){
        fragmentListViewModel.liveDataToDo.observe(
            viewLifecycleOwner, Observer {
                updateUI(it)
            }
        )
    }

    private fun removeObserver(){
        fragmentListViewModel.liveDataToDo.removeObservers(viewLifecycleOwner)
    }


    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun onCheckedChange(toDo: ToDo){
        toDo.isCompleted = !toDo.isCompleted

        //checkIsCompleted()

        fragmentListViewModel.updateToDo(toDo)
    }

//    fun checkIsCompleted(toDo: ToDo){
//        if (toDo.isCompleted) {
//            toDo.title.setTextColor(resources.getColor(fragmentListViewModel.black))
//
//            toDo.title.apply {
//                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            }
//        }else if (!toDo.isCompleted){
//            toDoTitle.apply {
//                paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//            }
//        }
//
//        val currentDate = Date()
//
//        if(toDo.dueDate != null) {
//            if (currentDate.after(toDo.dueDate) && !toDo.isCompleted) {
//                toDoTitle.setTextColor(resources.getColor(fragmentListViewModel.red))
//            }
//        }
//    }

    @Composable
    fun ToDoFragmentListComposable(toDos: List<ToDo>) {



        LazyColumn{

            itemsIndexed(
                toDos
            ){index , toDo ->


                Checkbox(checked = false, onCheckedChange = {  })


            }


        }
    }

    @Preview
    @Composable
    fun ToDoListFragmentPrev() {
        //ToDoFragmentListComposable()
    }



}