package com.example.myreminders.MVVM.View.Overdue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myreminders.MVVM.ViewModel.OverdueReminderViewModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.fragment_completed_reminders.view.*
import kotlinx.android.synthetic.main.fragment_overdue_reminders.view.*


class OverdueReminders : Fragment() {
    private lateinit var reminderViewModel : OverdueReminderViewModel
    private val adapter = OverdueReminderAdapter()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overdue_reminders, container, false)

        reminderViewModel = ViewModelProvider(this).get(OverdueReminderViewModel::class.java)

        val recycleView = view.overdueRecycler
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(requireContext())


        reminderViewModel.getAllOverdueReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if(adapter.itemCount > 0){
                view.nothingHereOverdue.visibility = View.GONE
            }
        })

        return view
    }


}