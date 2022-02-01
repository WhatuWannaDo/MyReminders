package com.example.myreminders.MVVM.View.Completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myreminders.MVVM.ViewModel.CompletedReminderViewModel
import com.example.myreminders.R


class CompletedReminders : Fragment() {

    private lateinit var viewModel : CompletedReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_reminders, container, false)

        viewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)



        return view
    }


}