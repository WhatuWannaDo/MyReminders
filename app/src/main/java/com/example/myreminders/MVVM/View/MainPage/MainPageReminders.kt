package com.example.myreminders.MVVM.View.MainPage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.widget.SearchView
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
import kotlinx.android.synthetic.main.bottom_sheet_choice.view.*
import kotlinx.android.synthetic.main.bottom_sheet_update_reminder.view.*
import kotlinx.android.synthetic.main.fragment_main_page_reminders.view.*
import kotlinx.android.synthetic.main.main_page_reminders_row.*
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainPageReminders : Fragment(), SearchView.OnQueryTextListener {

    private val adapter = MainPageAdapter()
    private lateinit var viewModel : ReminderViewModel
    private lateinit var completedViewModel : CompletedReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page_reminders, container, false)
        setHasOptionsMenu(true)

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        completedViewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)


        //update data by swipe
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            //update bottom sheet
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {


                val bottomSheetDialogChoice = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetViewChoice = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_choice, container, false)


                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
                val bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_update_reminder, container, false)

                //choiced update item in menu choice
                bottomSheetViewChoice.bottomSheetChoiceUpdate.setOnClickListener {
                    //setting data in bottom sheet
                    bottomSheetView.updateInputHeader.setText(viewHolder.itemView.rowHeader.text.toString())
                    bottomSheetView.updateInputDescirption.setText(viewHolder.itemView.goneDescription.text.toString())

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
                                val reminder = ReminderModel(
                                    Integer.parseInt(view.idForDelete.text.toString()),
                                    bottomSheetView.updateInputHeader.text.toString(),
                                    bottomSheetView.updateInputDescirption.text.toString(),
                                    getDateInMills(bottomSheetViewCalendar.getDate.text.toString()).toString(),
                                    view.rowStartTime.text.toString()
                                )

                                viewModel.updateReminder(reminder)
                                bottomSheetDialogCalendar.dismiss()
                                bottomSheetDialog.dismiss()
                                bottomSheetDialogChoice.dismiss()
                            }else{
                                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                            }
                        }

                        bottomSheetDialogCalendar.setContentView(bottomSheetViewCalendar)
                        bottomSheetDialogCalendar.show()
                    }

                    //update reminder from bottom_sheet_update without date change
                    bottomSheetView.updateReminderSheet.setOnClickListener {
                        if(bottomSheetView.updateInputHeader.text.toString().isNotEmpty() && bottomSheetView.updateInputDescirption.text.toString().isNotEmpty()) {
                            val reminder = ReminderModel(
                                Integer.parseInt(view.idForDelete.text.toString()),
                                bottomSheetView.updateInputHeader.text.toString(),
                                bottomSheetView.updateInputDescirption.text.toString(),
                                getDateInMills(view.rowEndTime.text.toString()).toString(),
                                view.rowStartTime.text.toString()
                            )

                            viewModel.updateReminder(reminder)
                            bottomSheetDialog.dismiss()
                            bottomSheetDialogChoice.dismiss()
                        }else{
                            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        }
                    }

                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()
                }

                //delete single data option menu
                bottomSheetViewChoice.bottomSheetChoiceDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(context)
                    builder.setPositiveButton("Да") { _, _ ->
                        deleteReminderFromDatabase()
                        bottomSheetDialogChoice.dismiss()
                    }
                    builder.setNegativeButton("Нет") { _, _ ->
                        adapter.notifyDataSetChanged()
                    }
                    builder.setMessage("Удалить это напоминание?")
                    builder.create().show()
                }


                bottomSheetViewChoice.bottomSheetChoiceReady.setOnClickListener {
                    val sdf = SimpleDateFormat("dd.M.yyyy")
                    val currentDate = sdf.format(Calendar.getInstance().time)
                    val completedReminder = CompletedReminderModel(
                        0,
                        viewHolder.itemView.rowHeader.text.toString(),
                        viewHolder.itemView.goneDescription.text.toString(),
                        currentDate
                    )
                    completedViewModel.addCompletedReminder(completedReminder)
                    deleteReminderFromDatabase()
                    bottomSheetDialogChoice.dismiss()
                }


                bottomSheetDialogChoice.setContentView(bottomSheetViewChoice)
                bottomSheetDialogChoice.show()
                adapter.notifyDataSetChanged()

            }

        }).attachToRecyclerView(view.recyclerView)



        viewModel.readAllReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if(adapter.itemCount > 0) {
                view.nothingHere.visibility = GONE
            }
        })

        return view
    }





    private fun deleteReminderFromDatabase() {
        val id = idForDelete.text.toString()
        val reminder = ReminderModel(Integer.parseInt(id), "", "", "", "")
        viewModel.deleteReminder(reminder)
    }

    //search and delete option menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.app_bar_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        val delete = menu.findItem(R.id.deleteAllFromMainPageRow)
        delete.setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteAllReminders()
                Toast.makeText(context, "Все напоминания удалены", Toast.LENGTH_SHORT)
                    .show()
            }
            builder.setNegativeButton("Нет") { _, _ -> }
            builder.setTitle("Удалить все напоминания?")
            builder.setMessage("Вы уверены что хотите это сделать?")
            builder.create().show()
            true
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            search(query)
        }
        return true
    }

    private fun search(query: String){
        val searchQuery = "%$query%"
        viewModel.findReminder(searchQuery).observe(viewLifecycleOwner, Observer {list ->
            list.let {
                adapter.setData(it)
            }
        })
    }
    @SuppressLint("SimpleDateFormat")
    fun getDateInMills(date : String) : Long{
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val dateParse = sdf.parse(date)
        val dateInMills = dateParse.time
        return dateInMills
    }
}