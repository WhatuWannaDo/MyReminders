package com.example.myreminders.Room.Repository

import androidx.lifecycle.LiveData
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.Room.Data.DAO.DAO

class Repository (private val remDao : DAO){
    val getAllReminders : LiveData<List<ReminderModel>> = remDao.getAllReminders()

    suspend fun addReminder(reminder : ReminderModel){
        remDao.addReminder(reminder)
    }

    suspend fun updateReminder(reminder: ReminderModel){
        remDao.updateReminder(reminder)
    }

    suspend fun deleteReminder(reminder: ReminderModel){
        remDao.deleteReminder(reminder)
    }

    fun deleteAllReminders(){
        remDao.deleteAllReminders()
    }

    suspend fun findReminder(search : String) : LiveData<List<ReminderModel>>{
        return remDao.findReminder(search)
    }
}