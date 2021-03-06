package toDoDialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import toDoFragment.DUE_DATE_KEY
import java.util.*

class DueDateDialog : DialogFragment() {

    interface DueDateCallBack{
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val date  = when (arguments?.getSerializable(DUE_DATE_KEY) as Date?) {
            null -> Date()
            else -> arguments?.getSerializable(DUE_DATE_KEY) as Date
        }

        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { _ , year , month, day ->
            val resultDate = GregorianCalendar(year,month,day).time

            targetFragment?.let {
                (it as DueDateCallBack).onDateSelected(resultDate)
            }

        }

        return DatePickerDialog(
            requireContext(),
            dateListener,
            year,
            month,
            day
        )

    }
}