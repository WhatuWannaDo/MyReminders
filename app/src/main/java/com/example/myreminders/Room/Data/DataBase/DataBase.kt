package com.example.myreminders.Room.Data.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.OverdueReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.Room.Data.DAO.DAO
import com.example.myreminders.Room.Data.DAO.DAOCompletedReminders
import com.example.myreminders.Room.Data.DAO.DAOOverdueReminders

@Database(entities = [ReminderModel::class, CompletedReminderModel::class, OverdueReminderModel::class], exportSchema = false, version = 4)
abstract class DataBase : RoomDatabase(){
    abstract fun reminderDao() : DAO
    abstract fun completedDao() : DAOCompletedReminders
    abstract fun overdueDao() : DAOOverdueReminders

    companion object{
        @Volatile
        private var INSTANCE : DataBase? = null

        fun getDatabase(context: Context): DataBase{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, DataBase::class.java, "reminder_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}