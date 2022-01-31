package com.example.myreminders.MVVM.View.MainPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myreminders.R
import kotlinx.android.synthetic.main.fragment_show_full_reminder.view.*


class ShowFullReminder : Fragment() {

    private val args by navArgs<ShowFullReminderArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_full_reminder, container, false)

        view.fullHeader.setText(args.argsForFullReminder.header)
        view.fullDescription.setText(args.argsForFullReminder.description)
        view.fullStartDate.setText(args.argsForFullReminder.startTime)
        view.fullEndDate.setText(args.argsForFullReminder.endTime)


        return view
    }


}