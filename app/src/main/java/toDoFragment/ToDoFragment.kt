package toDoFragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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
import androidx.sqlite.db.SupportSQLiteCompat.Api16Impl.cancel
import com.example.todo.R
import com.example.todo.ToDo
import toDoDialogs.DueDateDialog
import toDoListFragment.KEY_ID

import toDoListFragment.dateFormat
import java.util.*

const val DUE_DATE_KEY = "DueDate"

class ToDoFragment : Fragment() , DueDateDialog.DueDateCallBack{

    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var creationDateBtn: Button
    private lateinit var dueDateBtn: Button
    private lateinit var deleteBtn : Button
    private lateinit var saveBtn: Button

    private  var toDoId:UUID? = null

    private var toDo = ToDo ()

    val creationDateString = android.text.format.DateFormat.format(dateFormat,toDo.creationDate)


    private val fragmentViewModel by lazy { ViewModelProvider(this).get(ToDoFragmentViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        toDoId = arguments?.getSerializable(KEY_ID) as UUID?


        toDoId?.let {
            fragmentViewModel.loadToDo(it)
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
                     when (toDo.dueDate) {
                         null ->"enter due date"
                         else -> dueDateBtn.text = android.text.format.DateFormat.format(dateFormat,toDo.dueDate)
                     }
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
        saveBtn = view.findViewById(R.id.save_changes_Btn)

        creationDateBtn.isEnabled = false
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
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

        val descriptionWatcher = object : TextWatcher {
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

        deleteBtn.setOnClickListener {
            deleteToDo(toDo)

            fragmentViewModel.updateToDo(toDo)
        }

        dueDateBtn.setOnClickListener {
            val args = Bundle()
            val dueDate = DueDateDialog()

            args.putSerializable(DUE_DATE_KEY, toDo.dueDate)

            dueDate.arguments = args
            dueDate.setTargetFragment(this, 0)

            dueDate.show(this.parentFragmentManager, "DueDate")
        }

        saveBtn.setOnClickListener {
            if (toDoId == null) {


                saveToDo(toDo)
            } else {


                saveToDo(toDo)


            }
        }
    }



    private fun deleteToDo(toDo: ToDo){

        val builder = AlertDialog.Builder(activity)

        builder.setTitle("confirm delete")
        builder.setMessage("are you sure you want to delete this task?")

        builder.setPositiveButton("delete",DialogInterface.OnClickListener{ _, _ ->

            fragmentViewModel.deleteToDo(toDo)

            activity?.let {
                it.supportFragmentManager
                    .popBackStackImmediate()
            }
        })
        builder.setNegativeButton("cancel",DialogInterface.OnClickListener{ dialog, _ ->
        dialog.cancel()
        })
        val alert = builder.create()
        alert.show()



    }

    override fun onDateSelected(date: Date) {
        toDo.dueDate = date

        dueDateBtn.text = android.text.format.DateFormat.format(dateFormat,toDo.dueDate)
    }

    private fun saveToDo(toDo: ToDo){
        if (toDo.title == "" && toDoId == null){
            val builder = AlertDialog.Builder(activity)

            builder.setTitle("Empty title")
            builder.setMessage("are you sure you want to add a task with Empty title?")

            builder.setPositiveButton("add",DialogInterface.OnClickListener{ _, _ ->

                fragmentViewModel.addToDo(toDo)

                fragmentViewModel.updateToDo(toDo)


                activity?.let {
                    it.supportFragmentManager
                        .popBackStackImmediate()
                }
            })
            builder.setNegativeButton("cancel",DialogInterface.OnClickListener{ dialog, _ ->
                dialog.cancel()
            })
            val alert = builder.create()
            alert.show()
        }else if (toDoId == null){

            fragmentViewModel.addToDo(toDo)

            fragmentViewModel.updateToDo(toDo)


            activity?.let {
                it.supportFragmentManager
                    .popBackStackImmediate()
            }
        }else{
            fragmentViewModel.updateToDo(toDo)


            activity?.let {
                it.supportFragmentManager
                    .popBackStackImmediate()
            }
        }
    }


}