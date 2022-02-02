package com.example.myreminders.MVVM.View.Completed

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.MVVM.ViewModel.CompletedReminderViewModel
import com.example.myreminders.MVVM.ViewModel.ReminderViewModel
import com.example.myreminders.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.bottom_sheet_update_reminder.view.*
import kotlinx.android.synthetic.main.completed_reminders_row.*
import kotlinx.android.synthetic.main.completed_reminders_row.view.*
import kotlinx.android.synthetic.main.fragment_completed_reminders.*
import kotlinx.android.synthetic.main.fragment_completed_reminders.view.*
import kotlinx.android.synthetic.main.main_page_reminders_row.*
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class CompletedReminders : Fragment() {

    private lateinit var viewModel : CompletedReminderViewModel
    private lateinit var reminderViewModel : ReminderViewModel
    private val adapter = CompletedReminderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_reminders, container, false)

        viewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)
        reminderViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)

        val recycleView = view.completedRecycler
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.getAllCompletedReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if (adapter.itemCount > 0){
                view.completedNothingHere.visibility = View.GONE
            }
        })

        //delete single data on swipe
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteSingleDataFromDatabase()
            }
        }).attachToRecyclerView(view.completedRecycler)


        //retrieve reminder from completed to main page
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_update_reminder, container, false)

                bottomSheetView.updateReminderSheet.visibility = View.GONE

                bottomSheetView.updateInputHeader.setText(viewHolder.itemView.completedHeaderRow.text.toString())
                bottomSheetView.updateInputDescirption.setText(viewHolder.itemView.completeDescription.text.toString())

                //sheet view with calendar then save reminder
                bottomSheetView.updateCalendarDate.setOnClickListener {
                    val bottomSheetDialogCalendar = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                    val bottomSheetViewCalendar = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, container, false)

                    bottomSheetViewCalendar.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
                        val realMonth = month+1
                        bottomSheetViewCalendar.getDate.text = "$day.$realMonth.$year"
                    }

                    bottomSheetViewCalendar.saveAddedReminder.setOnClickListener {

                        val sdf = SimpleDateFormat("dd.M.yyyy")
                        val currentDate = sdf.format(Calendar.getInstance().time)
                        if(bottomSheetView.updateInputHeader.text.toString().isNotEmpty() && bottomSheetView.updateInputDescirption.text.toString().isNotEmpty()) {
                            val reminder = ReminderModel(
                                Integer.parseInt(view.completeIdForDelete.text.toString()),
                                bottomSheetView.updateInputHeader.text.toString(),
                                bottomSheetView.updateInputDescirption.text.toString(),
                                getDateInMills(bottomSheetViewCalendar.getDate.text.toString()).toString(),
                                currentDate
                            )

                            reminderViewModel.addReminder(reminder)
                            deleteSingleDataFromDatabase()
                            bottomSheetDialogCalendar.dismiss()
                            bottomSheetDialog.dismiss()
                            adapter.notifyDataSetChanged()

                        }else{
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    }


                    bottomSheetDialogCalendar.setContentView(bottomSheetViewCalendar)
                    bottomSheetDialogCalendar.show()
                }
                adapter.notifyDataSetChanged()

                bottomSheetDialog.setContentView(bottomSheetView)
                bottomSheetDialog.show()
            }
        }).attachToRecyclerView(view.completedRecycler)

        return view
    }

    private fun deleteSingleDataFromDatabase(){
        val reminder = CompletedReminderModel(Integer.parseInt(completeIdForDelete.text.toString()), "", "", "")
        viewModel.deleteCompletedReminder(reminder)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateInMills(date : String) : Long{
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val dateParse = sdf.parse(date)
        val dateInMills = dateParse.time
        return dateInMills
    }
}