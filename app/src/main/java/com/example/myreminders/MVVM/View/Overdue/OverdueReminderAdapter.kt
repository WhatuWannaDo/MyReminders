package com.example.myreminders.MVVM.View.Overdue

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.overdue_reminders_row.view.*

class OverdueReminderAdapter : RecyclerView.Adapter<OverdueReminderAdapter.MyOverdueViewHolder>() {
    class MyOverdueViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}

    private var remList = emptyList<OverdueReminderModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOverdueViewHolder {
        return MyOverdueViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.overdue_reminders_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyOverdueViewHolder, position: Int) {
        val list = remList[position]

        holder.itemView.headerForOverdue.setText(list.header)
        holder.itemView.endDateOverdue.setText(list.overdueTime)

        holder.itemView.idForOverdue.setText(list.id.toString())
        holder.itemView.descriptionForOverdue.setText(list.description)

        holder.itemView.overdueItemsConstraint.setOnClickListener {
            val action = OverdueRemindersDirections.actionOverdueRemindersToShowFullOverdueReminder(list)
            holder.itemView.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return remList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(reminder : List<OverdueReminderModel>){
        this.remList = reminder
        notifyDataSetChanged()
    }
}