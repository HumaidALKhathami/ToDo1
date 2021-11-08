package toDoFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.todo.R
import com.example.todo.ToDo
import toDoListFragment.KEY_ID
import toDoListFragment.dateFormat
import java.util.*


class ToDoFragment : Fragment() {

    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var creationDateBtn: Button
    private lateinit var dueDateBtn: Button
    private lateinit var deleteBtn : Button

    private var toDo = ToDo ()

    val creationDateString = android.text.format.DateFormat.format(dateFormat,toDo.creationDate)
    val dueDateString = android.text.format.DateFormat.format(dateFormat,toDo.dueDate)

    private val fragmentViewModel by lazy { ViewModelProvider(this).get(ToDoFragmentViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val toDoId = arguments?.getSerializable(KEY_ID) as UUID?

        if (toDoId != null) {
            fragmentViewModel.loadToDo(toDoId)
        }

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_do, container, false)
            initialize(view)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentViewModel.toDoLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    toDo = it
                    title.setText(it.title)
                    description.setText(it.description)
                    creationDateBtn.text = creationDateString
                    dueDateBtn.text = dueDateString
                }
            }
        )
    }

    private fun initialize(view: View){
        title = view.findViewById(R.id.to_do_title)
        description = view.findViewById(R.id.to_do_description)
        creationDateBtn = view.findViewById(R.id.creation_date_btn)
        dueDateBtn = view.findViewById(R.id.due_date_btn)
        deleteBtn = view.findViewById(R.id.delete_button)
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing to do here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toDo.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // nothing to do here
            }

        }

        val descriptionWatcher = object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing to do here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                toDo.description = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // nothing to do here
            }

        }

        title.addTextChangedListener(titleWatcher)
        description.addTextChangedListener(descriptionWatcher)


    }

    override fun onStop() {
        super.onStop()

        fragmentViewModel.saveUpdates(toDo)
    }


}