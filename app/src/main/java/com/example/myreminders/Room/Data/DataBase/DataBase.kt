package com.example.myreminders.Room.Data.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myreminders.MVVM.Model.CompletedReminderModel
import com.example.myreminders.MVVM.Model.ReminderModel
import com.example.myreminders.Room.Data.DAO.DAO
import com.example.myreminders.Room.Data.DAO.DAOCompletedReminders

@Database(entities = [ReminderModel::class, CompletedReminderModel::class], exportSchema = false, version = 2)
abstract class DataBase : RoomDatabase(){
    abstract fun reminderDao() : DAO
    abstract fun completedDao() : DAOCompletedReminders

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