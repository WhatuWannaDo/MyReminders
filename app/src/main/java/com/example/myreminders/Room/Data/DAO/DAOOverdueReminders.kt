package com.example.myreminders.Room.Data.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myreminders.MVVM.Model.OverdueReminderModel

@Dao
interface DAOOverdueReminders {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOverdueReminder(reminderModel : OverdueReminderModel)

    @Query("SELECT * FROM overdue_reminders ORDER BY overdueTime")
    fun getAllReminders() : LiveData<List<OverdueReminderModel>>

    @Delete
    suspend fun deleteOverdueReminder(reminderModel: OverdueReminderModel)

}