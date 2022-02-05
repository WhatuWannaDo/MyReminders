package com.example.myreminders.MVVM.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.Room.Data.DataBase.DataBase
import com.example.myreminders.Room.Repository.RepositoryOverdueReminders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OverdueReminderViewModel(application: Application) : AndroidViewModel(application) {

    val getAllOverdueReminders : LiveData<List<OverdueReminderModel>>
    private val repository : RepositoryOverdueReminders

    init {
        val overdueDAO = DataBase.getDatabase(application).overdueDao()
        repository = RepositoryOverdueReminders(overdueDAO)
        getAllOverdueReminders = repository.getAllOverdueReminders
    }

    fun addOverdueReminder(overdueReminderModel: OverdueReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addReminder(overdueReminderModel)
        }
    }

    fun deleteOverdueReminder(overdueReminderModel: OverdueReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReminder(overdueReminderModel)
        }
    }
}