package com.example.myreminders.MVVM.View.MainPage

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.MVVM.ViewModel.ReminderViewModel
import com.example.myreminders.R
import kotlinx.android.synthetic.main.fragment_main_page_reminders.view.*
import kotlinx.android.synthetic.main.main_page_reminders_row.*


class MainPageReminders : Fragment(), SearchView.OnQueryTextListener {

    private val adapter = MainPageAdapter()
    private lateinit var viewModel : ReminderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page_reminders, container, false)
        setHasOptionsMenu(true)

        val recyclerView = view.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)



        //delete single data on swipe
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteReminderFromDatabase()
            }

        }).attachToRecyclerView(view.recyclerView)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //TODO: обновить свайпнутое напоминание с помощью bottom_sheet или редиректом на другую страницу с помощью safe args
            }

        }).attachToRecyclerView(view.recyclerView)



        viewModel.readAllReminders.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            if(adapter.itemCount > 0) {
                view.nothingHere.visibility = GONE
            }
        })

        return view
    }





    private fun deleteReminderFromDatabase() {
        val id = idForDelete.text.toString()
        val reminder = ReminderModel(Integer.parseInt(id), "", "", "", "")
        viewModel.deleteReminder(reminder)
    }

    //search and delete option menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.app_bar_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        val delete = menu.findItem(R.id.deleteAllFromMainPageRow)
        delete.setOnMenuItemClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setPositiveButton("Да") { _, _ ->
                viewModel.deleteAllReminders()
                Toast.makeText(context, "Все пользователи удалены", Toast.LENGTH_SHORT)
                    .show()
            }
            builder.setNegativeButton("Нет") { _, _ -> }
            builder.setTitle("Удалить всех пользователей?")
            builder.setMessage("Вы уверены что хотите это сделать?")
            builder.create().show()
            true
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null){
            search(query)
        }
        return true
    }

    private fun search(query: String){
        val searchQuery = "%$query%"
        viewModel.findReminder(searchQuery).observe(viewLifecycleOwner, Observer {list ->
            list.let {
                adapter.setData(it)
            }
        })
    }

}