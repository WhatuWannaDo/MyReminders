package com.example.myreminders.Room.Data.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myreminders.MVVM.Model.CompletedReminderModel

@Dao
interface DAOCompletedReminders {
    @Query("SELECT * FROM completed_reminders ORDER BY completedTime ASC")
    fun getAllCompletedReminders() : LiveData<List<CompletedReminderModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCompletedReminder(reminder : CompletedReminderModel)

    @Delete
    suspend fun deleteCompletedReminder(reminder: CompletedReminderModel)


}