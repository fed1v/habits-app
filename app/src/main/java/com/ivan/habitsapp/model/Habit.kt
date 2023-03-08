package com.ivan.habitsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Habit(
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: String,
    val periodicity: Int,
    val color: Int
) : Parcelable