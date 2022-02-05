package com.example.myreminders.MVVM.View.MainPage

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.MVVM.ViewModel.CompletedReminderViewModel
import com.example.myreminders.MVVM.ViewModel.OverdueReminderViewModel
import com.example.myreminders.MVVM.ViewModel.ReminderViewModel
import com.example.myreminders.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.bottom_sheet_choice.view.*
import kotlinx.android.synthetic.main.bottom_sheet_update_reminder.view.*
import kotlinx.android.synthetic.main.fragment_main_page_reminders.view.*
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myreminders.MainActivity


class MainPageReminders : Fragment(), SearchView.OnQueryTextListener {

    private val adapter = MainPageAdapter()
    private lateinit var viewModel : ReminderViewModel
    private lateinit var completedViewModel : CompletedReminderViewModel
    private lateinit var overdueViewModel: OverdueReminderViewModel

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIFICATION_ID = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page_reminders, container, false)

        setHasOptionsMenu(true)

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        completedViewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)
        overdueViewModel = ViewModelProvider(this).get(OverdueReminderViewModel::class.java)



        //update data by swipe
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            //choice bottom sheet
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
                                    Integer.parseInt(viewHolder.itemView.idForDelete.text.toString()),
                                    bottomSheetView.updateInputHeader.text.toString(),
                                    bottomSheetView.updateInputDescirption.text.toString(),
                                    getDateInMills(bottomSheetViewCalendar.getDate.text.toString()).toString(),
                                    viewHolder.itemView.rowStartTime.text.toString()
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
                                Integer.parseInt(viewHolder.itemView.idForDelete.text.toString()),
                                bottomSheetView.updateInputHeader.text.toString(),
                                bottomSheetView.updateInputDescirption.text.toString(),
                                getDateInMills(viewHolder.itemView.rowEndTime.text.toString()).toString(),
                                viewHolder.itemView.rowStartTime.text.toString()
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
                        deleteReminderFromDatabase(Integer.parseInt(viewHolder.itemView.idForDelete.text.toString()))
                        bottomSheetDialogChoice.dismiss()
                    }
                    builder.setNegativeButton("Нет") { _, _ ->
                        adapter.notifyDataSetChanged()
                    }
                    builder.setMessage("Удалить это напоминание?")
                    builder.create().show()
                }

                //add reminder into completed
                bottomSheetViewChoice.bottomSheetChoiceReady.setOnClickListener {
                    val currentDate = Calendar.getInstance().timeInMillis
                    val completedReminder = CompletedReminderModel(
                        0,
                        viewHolder.itemView.rowHeader.text.toString(),
                        viewHolder.itemView.goneDescription.text.toString(),
                        currentDate.toString()
                    )
                    completedViewModel.addCompletedReminder(completedReminder)
                    deleteReminderFromDatabase(Integer.parseInt(viewHolder.itemView.idForDelete.text.toString()))
                    bottomSheetDialogChoice.dismiss()
                }


                bottomSheetDialogChoice.setContentView(bottomSheetViewChoice)
                bottomSheetDialogChoice.show()
                adapter.notifyDataSetChanged()

            }

        }).attachToRecyclerView(view.recyclerView)

        //pending intent
        val intent = Intent(requireContext(), MainActivity::class.java)
        val sendIntent = TaskStackBuilder.create(requireContext()).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        viewModel.readAllReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if(adapter.itemCount > 0) {
                view.nothingHere.visibility = GONE
            }
            adapter.overdueListener { data ->
                overdueViewModel.addOverdueReminder(data)
                //added notify
                createNotifyChannel()
                val notify = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                    .setContentTitle("Уведомление")
                    .setContentText("У вас появилось просроченное напоминание!")
                    .setSmallIcon(R.drawable.ic_baseline_edit_calendar_24)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(sendIntent)
                    .build()
                val notifyManager = NotificationManagerCompat.from(requireContext())
                notifyManager.notify(NOTIFICATION_ID, notify)
            }
            adapter.reminderListener { deleteData ->
                viewModel.deleteReminder(deleteData)
            }

        })

        return view
    }

    fun createNotifyChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val notificationManager : NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }



    private fun deleteReminderFromDatabase(id : Int) {
        val reminder = ReminderModel(id, "", "", "", "")
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