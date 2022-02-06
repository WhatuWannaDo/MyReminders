package com.example.myreminders.MVVM.View.Overdue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.myreminders.R
import kotlinx.android.synthetic.main.fragment_show_full_completed_reminder.view.*
import java.text.SimpleDateFormat

class ShowFullOverdueReminder : Fragment() {
    private val args by navArgs<ShowFullOverdueReminderArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_show_full_completed_reminder, container, false)
        val sdf = SimpleDateFormat("dd.M.yyyy")
        view.fullCompleteHeader.setText(args.argsForOverdueReminder.header)
        view.fullCompleteDescription.setText(args.argsForOverdueReminder.description)
        view.fullFinishDate.setText(sdf.format(args.argsForOverdueReminder.overdueTime.toLong()))

        return view
    }

}