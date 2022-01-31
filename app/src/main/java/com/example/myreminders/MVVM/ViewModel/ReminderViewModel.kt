package com.example.myreminders.MVVM.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.Room.Data.DataBase.DataBase
import com.example.myreminders.Room.Repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    val readAllReminders : LiveData<List<ReminderModel>>
    private val repository : Repository

    init {
        val reminderDAO = DataBase.getDatabase(application).reminderDao()
        repository = Repository(reminderDAO)
        readAllReminders = repository.getAllReminders
    }

    fun addReminder(reminder: ReminderModel){
        viewModelScope.launch (Dispatchers.IO){
            repository.addReminder(reminder)
        }
    }

    fun updateReminder(reminder: ReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: ReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(reminder)
        }
    }
    fun deleteAllReminders(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllReminders()
        }
    }
    fun findReminder(search: String): LiveData<List<ReminderModel>>{
        return repository.findReminder(search)

    }



}