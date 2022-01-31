package com.example.myreminders.MVVM.View.MainPage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.main_page_reminders_row.view.*

class MainPageAdapter : RecyclerView.Adapter<MainPageAdapter.MyViewHolder>() {

    private var remList = emptyList<ReminderModel>()

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.main_page_reminders_row, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = remList[position]

        holder.itemView.idForDelete.text = list.id.toString()
        holder.itemView.rowHeader.text = list.header
        holder.itemView.rowStartTime.text = list.startTime
        holder.itemView.rowEndTime.text = list.endTime

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