package com.example.myreminders.Room.Repository

import androidx.lifecycle.LiveData
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.Room.Data.DAO.DAOOverdueReminders

class RepositoryOverdueReminders(private val overdueDAO : DAOOverdueReminders) {
    val getAllOverdueReminders : LiveData<List<OverdueReminderModel>> = overdueDAO.getAllReminders()

    suspend fun addReminder(reminderModel: OverdueReminderModel){
        overdueDAO.addOverdueReminder(reminderModel)
    }

    suspend fun deleteReminder(reminderModel: OverdueReminderModel){
        overdueDAO.deleteOverdueReminder(reminderModel)
    }
}