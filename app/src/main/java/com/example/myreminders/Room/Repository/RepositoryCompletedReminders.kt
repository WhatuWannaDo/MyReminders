package com.example.myreminders.Room.Repository

import androidx.lifecycle.LiveData
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.Room.Data.DAO.DAOCompletedReminders

class RepositoryCompletedReminders(private val compDao: DAOCompletedReminders) {
    val getAllCompletedReminders : LiveData<List<CompletedReminderModel>> = compDao.getAllCompletedReminders()

    suspend fun addCompletedReminder(completedReminder: CompletedReminderModel){
        compDao.addCompletedReminder(completedReminder)
    }

    suspend fun updateCompletedReminder(completedReminder: CompletedReminderModel){
        compDao.updateCompletedReminder(completedReminder)
    }

    suspend fun deleteCompletedReminder(completedReminder: CompletedReminderModel){
        compDao.deleteCompletedReminder(completedReminder)
    }

    suspend fun deleteAllCompletedReminders(){
        compDao.deleteAllCompletedReminders()
    }
}