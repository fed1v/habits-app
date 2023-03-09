package com.ivan.habitsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val periodicity: HabitPeriodicity,
    val color: Int
) : Parcelable