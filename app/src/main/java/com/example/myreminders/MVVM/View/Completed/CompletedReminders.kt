package com.example.myreminders.MVVM.View.Completed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.ViewModel.CompletedReminderViewModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.completed_reminders_row.*
import kotlinx.android.synthetic.main.fragment_completed_reminders.*
import kotlinx.android.synthetic.main.fragment_completed_reminders.view.*
import kotlinx.android.synthetic.main.main_page_reminders_row.*


class CompletedReminders : Fragment() {

    private lateinit var viewModel : CompletedReminderViewModel
    private val adapter = CompletedReminderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_reminders, container, false)

        viewModel = ViewModelProvider(this).get(CompletedReminderViewModel::class.java)

        val recycleView = view.completedRecycler
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(requireContext())


        viewModel.getAllCompletedReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if (adapter.itemCount > 0){
                view.completedNothingHere.visibility = View.GONE
            }
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteSingleDataFromDatabase()
            }
        }).attachToRecyclerView(view.completedRecycler)

        view.completedRecycler.setOnClickListener {

        }

        return view
    }

    private fun deleteSingleDataFromDatabase(){
        val reminder = CompletedReminderModel(Integer.parseInt(completeIdForDelete.text.toString()), "", "", "")
        viewModel.deleteCompletedReminder(reminder)
    }


}