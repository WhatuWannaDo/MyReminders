package com.example.myreminders.MVVM.View.Overdue

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.MVVM.ViewModel.CompletedReminderViewModel
import com.example.myreminders.MVVM.ViewModel.OverdueReminderViewModel
import com.example.myreminders.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.bottom_sheet_update_reminder.view.*
import kotlinx.android.synthetic.main.fragment_overdue_reminders.view.*
import kotlinx.android.synthetic.main.overdue_reminders_row.view.*
import java.text.SimpleDateFormat


class OverdueReminders : Fragment() {
    private lateinit var reminderViewModel : OverdueReminderViewModel
    private lateinit var completedViewModel : CompletedReminderViewModel
    private val adapter = OverdueReminderAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overdue_reminders, container, false)

        reminderViewModel = ViewModelProvider(this).get(OverdueReminderViewModel::class.java)
        completedViewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)

        val recycleView = view.overdueRecycler
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(requireContext())

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_update_reminder, container, false)

                bottomSheetView.updateInputHeader.setText(viewHolder.itemView.headerForOverdue.text.toString())
                bottomSheetView.updateInputDescirption.setText(viewHolder.itemView.descriptionForOverdue.text.toString())

                //calendar click listener
                bottomSheetView.updateCalendarDate.setOnClickListener {
                    val bottomSheetDialogCalendar = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                    val bottomSheetViewCalendar = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet, container, false)

                    //edit calendar date
                    bottomSheetViewCalendar.calendarView.setOnDateChangeListener { calendar, year, month, day ->
                        val realMonth = month+1
                        bottomSheetViewCalendar.getDate.text = "$day.$realMonth.$year"
                    }

                    //update reminder from calendar bottom sheet with date change
                    bottomSheetViewCalendar.saveAddedReminder.setOnClickListener {
                        if(bottomSheetView.updateInputHeader.text.toString().isNotEmpty() && bottomSheetView.updateInputDescirption.text.toString().isNotEmpty()) {
                            val reminder = CompletedReminderModel(
                                Integer.parseInt(viewHolder.itemView.idForOverdue.text.toString()),
                                bottomSheetView.updateInputHeader.text.toString(),
                                bottomSheetView.updateInputDescirption.text.toString(),
                                getDateInMills(bottomSheetViewCalendar.getDate.text.toString()).toString(),
                            )

                            completedViewModel.addCompletedReminder(reminder)
                            deleteReminderFromDatabase(Integer.parseInt(viewHolder.itemView.idForOverdue.text.toString()))
                            bottomSheetDialogCalendar.dismiss()
                            bottomSheetDialog.dismiss()
                        }else{
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    }

                    bottomSheetDialogCalendar.setContentView(bottomSheetViewCalendar)
                    bottomSheetDialogCalendar.show()
                }

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }

        }).attachToRecyclerView(view.overdueRecycler)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteReminderFromDatabase(Integer.parseInt(viewHolder.itemView.idForOverdue.text.toString()))
            }

        }).attachToRecyclerView(view.overdueRecycler)

        reminderViewModel.getAllOverdueReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if(adapter.itemCount > 0){
                view.nothingHereOverdue.visibility = View.GONE
            }
        })

        return view
    }


    private fun deleteReminderFromDatabase(id : Int) {
        val reminder = OverdueReminderModel(id, "", "", "")
        reminderViewModel.deleteOverdueReminder(reminder)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateInMills(date : String) : Long{
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val dateParse = sdf.parse(date)
        val dateInMills = dateParse.time
        return dateInMills
    }
}