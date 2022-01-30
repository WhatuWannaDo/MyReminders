package com.example.myreminders.MVVM.Model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "reminders")
data class ReminderModel (
    val header : String,
    val description : String,
    val endTime : String,
    val startTime : String
) : Parcelable