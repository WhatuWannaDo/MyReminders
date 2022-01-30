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
    fun deleteAllReminders()

    @Query("SELECT * FROM reminders WHERE header OR description OR startTime OR endTime LIKE :searchQuery")
    suspend fun findReminder(searchQuery : String): LiveData<List<ReminderModel>>
}