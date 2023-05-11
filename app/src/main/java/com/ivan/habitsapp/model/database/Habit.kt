package com.ivan.habitsapp.model.database

import android.graphics.Color
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ivan.habitsapp.model.HabitPeriodicity
import com.ivan.habitsapp.model.HabitPriority
import com.ivan.habitsapp.model.HabitType
import com.ivan.habitsapp.util.Converters
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "habits_table")
@TypeConverters(Converters::class)
data class Habit(
    @PrimaryKey
    val id: Int? = null,
    var uid: String = "",
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val date: Int,
    val periodicity: HabitPeriodicity,
    val color: Int = Color.parseColor("#66BB6A"),
    val isSynced: Boolean = false
) : Parcelable