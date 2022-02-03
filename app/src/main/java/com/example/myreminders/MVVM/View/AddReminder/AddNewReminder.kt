package com.example.myreminders.MVVM.View.AddReminder

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.MVVM.ViewModel.OverdueReminderViewModel
import com.example.myreminders.MVVM.ViewModel.ReminderViewModel
import com.example.myreminders.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_add_new_reminder.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddNewReminder : Fragment() {

    private lateinit var viewModel : ReminderViewModel

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_new_reminder, container, false)
        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        //bottom sheet use
        view.addContinue.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet, container, false)


            bottomSheetView.calendarView.setOnDateChangeListener { calendar, year, month, day ->
                val realMonth = month+1
                bottomSheetView.getDate.text = "$day.$realMonth.$year"

            }

            //data add
            bottomSheetView.saveAddedReminder.setOnClickListener {
                val sdf = SimpleDateFormat("dd.M.yyyy")
                val currentDate = sdf.format(Calendar.getInstance().time)
                if(view.addInputHeader.text.toString().isNotEmpty() && view.addInputDescirption.text.toString().isNotEmpty()) {
                    val reminder = ReminderModel(
                        0,
                        view.addInputHeader.text.toString(),
                        view.addInputDescirption.text.toString(),
                        getDateInMills(bottomSheetView.getDate.text.toString()).toString(),
                        currentDate
                    )

                    viewModel.addReminder(reminder)
                    Navigation.findNavController(view).navigate(R.id.action_addNewReminder_to_mainPageReminders)
                    bottomSheetDialog.dismiss()
                }else{
                    Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                }
            }


            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        return view
    }


    @SuppressLint("SimpleDateFormat")
    fun getDateInMills(date : String) : Long{
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val dateParse = sdf.parse(date)
        val dateInMills = dateParse.time
        return dateInMills
    }

}