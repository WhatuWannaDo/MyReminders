package com.example.myreminders.MVVM.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.Room.Data.DataBase.DataBase
import com.example.myreminders.Room.Repository.RepositoryCompletedReminders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CompletedReminderViewModel(application: Application) : AndroidViewModel(application) {
    val getAllCompletedReminders : LiveData<List<CompletedReminderModel>>
    private val repository : RepositoryCompletedReminders

    init {
        val completedDAO = DataBase.getDatabase(application).completedDao()
        repository = RepositoryCompletedReminders(completedDAO)
        getAllCompletedReminders = repository.getAllCompletedReminders
    }

    fun addCompletedReminder(completedReminderModel: CompletedReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCompletedReminder(completedReminderModel)
        }
    }

    fun deleteCompletedReminder(completedReminderModel: CompletedReminderModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCompletedReminder(completedReminderModel)
        }
    }

}