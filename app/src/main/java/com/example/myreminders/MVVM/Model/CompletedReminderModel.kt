package com.example.myreminders.MVVM.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "completed_reminders")
data class CompletedReminderModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val header: String,
    val description: String,
    val completedTime: String
    ): Parcelable