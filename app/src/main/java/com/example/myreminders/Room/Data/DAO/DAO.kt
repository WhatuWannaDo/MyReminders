package com.example.myreminders.Room.Data.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myreminders.MVVM.Model.ReminderModel

@Dao
interface DAO {

    @Query("SELECT * FROM reminders ORDER BY endTime ASC")
    fun getAllReminders(): LiveData<List<ReminderModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addReminder(reminder : ReminderModel)

    @Update
    suspend fun updateReminder(reminder: ReminderModel)

    @Delete
    suspend fun deleteReminder(reminder: ReminderModel)

    @Query("DELETE FROM reminders")
    suspend fun deleteAllReminders()

    @Query("SELECT * FROM reminders WHERE header LIKE :searchQuery OR description LIKE :searchQuery OR startTime LIKE :searchQuery OR endTime LIKE :searchQuery")
    fun findReminder(searchQuery : String): LiveData<List<ReminderModel>>
}