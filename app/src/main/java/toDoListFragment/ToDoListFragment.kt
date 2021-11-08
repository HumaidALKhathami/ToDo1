package toDoListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.ToDo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import toDoFragment.ToDoFragment

const val KEY_ID  = "ToDoID"

class ToDoListFragment : Fragment() {

    private val fragmentListViewModel by lazy { ViewModelProvider(this).get(ToDoListViewModel::class.java) }

    private lateinit var toDoRv : RecyclerView
    private lateinit var addToDo: FloatingActionButton

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

        fragmentListViewModel.liveDataToDo.observe(
            viewLifecycleOwner, Observer {
                updateUI(it)
            }
        )
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

            toDoTitle.text = toDo.title
            toDoCreationDate.text = toDo.creationDate.toString()
            toDoDueDate.text = toDo.dueDate.toString()


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

    fun addToDo(){
        addToDo.setOnClickListener {

            val toDo = ToDo()
            val args = Bundle()
            val fragment = ToDoFragment()

            fragmentListViewModel.addtoDo(toDo)
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


}