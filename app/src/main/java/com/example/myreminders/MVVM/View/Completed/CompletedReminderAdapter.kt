package com.example.myreminders.MVVM.View.Completed

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.completed_reminders_row.view.*
import kotlinx.android.synthetic.main.fragment_completed_reminders.view.*
import java.text.SimpleDateFormat

class CompletedReminderAdapter : RecyclerView.Adapter<CompletedReminderAdapter.MyCompletedViewHolder>() {


    private var remList = emptyList<CompletedReminderModel>()

    class MyCompletedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCompletedViewHolder {
        return MyCompletedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.completed_reminders_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyCompletedViewHolder, position: Int) {
        val list = remList[position]

        val sdf = SimpleDateFormat("dd.M.yyyy")

        holder.itemView.completedHeaderRow.setText(list.header)
        holder.itemView.completedDateRow.setText(sdf.format(list.completedTime.toLong()))

        holder.itemView.completeIdForDelete.setText(list.id.toString())
        holder.itemView.completeDescription.setText(list.description)

        holder.itemView.completedFullConstraint.setOnClickListener {
            val action = CompletedRemindersDirections.actionCompletedReminders2ToShowFullCompletedReminder2(list)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return remList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(reminder : List<CompletedReminderModel>){
        this.remList = reminder
        notifyDataSetChanged()
    }

}