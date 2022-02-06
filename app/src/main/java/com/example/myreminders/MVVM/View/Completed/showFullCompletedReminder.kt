package com.example.myreminders.MVVM.View.Completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.myreminders.R
import kotlinx.android.synthetic.main.fragment_show_full_completed_reminder.view.*
import java.text.SimpleDateFormat


class showFullCompletedReminder : Fragment() {
    private val arguments by navArgs<showFullCompletedReminderArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_show_full_completed_reminder, container, false)

        val sdf = SimpleDateFormat("dd.M.yyyy")
        view.fullCompleteHeader.setText(arguments.argsForFullCompleted.header)
        view.fullCompleteDescription.setText(arguments.argsForFullCompleted.description)
        view.fullFinishDate.setText(sdf.format(arguments.argsForFullCompleted.completedTime.toLong()))

        return view
    }
}

