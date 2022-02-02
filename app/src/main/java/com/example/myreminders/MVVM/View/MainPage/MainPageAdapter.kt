package com.example.myreminders.MVVM.View.MainPage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class MainPageAdapter : RecyclerView.Adapter<MainPageAdapter.MyViewHolder>() {

    private var remList = emptyList<ReminderModel>()

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

    }

    override fun getItemCount(): Int {
        return remList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(reminder : List<ReminderModel>){
        this.remList = reminder
        notifyDataSetChanged()
    }
}