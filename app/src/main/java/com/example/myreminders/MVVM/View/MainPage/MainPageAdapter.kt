package com.example.myreminders.MVVM.View.MainPage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainPageAdapter : RecyclerView.Adapter<MainPageAdapter.MyViewHolder>() {

    private var remList = emptyList<ReminderModel>()
    private lateinit var overdueReminder : OverdueReminderModel
    private lateinit var overdueCallback : (item : OverdueReminderModel) -> Unit

    private lateinit var deleteReminder : ReminderModel
    private lateinit var reminderCallback : (item : ReminderModel) -> Unit

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_page_reminders_row, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = remList[position]

        val sdf = SimpleDateFormat("dd.M.yyyy")

        holder.itemView.idForDelete.text = list.id.toString()
        holder.itemView.rowHeader.text = list.header
        holder.itemView.goneDescription.text = list.description
        holder.itemView.rowStartTime.text = list.startTime
        holder.itemView.rowEndTime.text = sdf.format(list.endTime.toLong())

        //redirect with safe args to show reminder
        holder.itemView.rowOfMainPageItems.setOnClickListener {
            val action = MainPageRemindersDirections.actionMainPageRemindersToShowFullReminder(list)
            holder.itemView.findNavController().navigate(action)
        }

        val currentDate = Calendar.getInstance().timeInMillis.toString()
        //move data in overdue
        if(currentDate.toLong() > getDateInMills(holder.itemView.rowEndTime.text.toString())){
            overdueReminder = OverdueReminderModel(
                0,
                holder.itemView.rowHeader.text.toString(),
                holder.itemView.goneDescription.text.toString(),
                getDateInMills(holder.itemView.rowEndTime.text.toString()).toString()
            )

            deleteReminder = ReminderModel(Integer.parseInt(holder.itemView.idForDelete.text.toString()), "", "", "", "")

            reminderCallback(deleteReminder)
            overdueCallback(overdueReminder)

        }
    }

    fun overdueListener(callback: (item: OverdueReminderModel) -> Unit){
        overdueCallback = callback
    }

    fun reminderListener(callback: (item: ReminderModel) -> Unit){
        reminderCallback = callback
    }

    override fun getItemCount(): Int {
        return remList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(reminder : List<ReminderModel>){
        this.remList = reminder
        notifyDataSetChanged()
    }


    @SuppressLint("SimpleDateFormat")
    fun getDateInMills(date : String) : Long{
        val sdf = SimpleDateFormat("dd.M.yyyy")
        val dateParse = sdf.parse(date)
        val dateInMills = dateParse.time
        return dateInMills
    }
}