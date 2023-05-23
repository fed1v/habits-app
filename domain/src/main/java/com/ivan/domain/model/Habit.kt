package com.ivan.domain.model

import android.graphics.Color

data class Habit(
    val id: Int? = null,
    var uid: String = "",
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val date: Int,
    val periodicity: HabitPeriodicity,
    val color: Int = Color.parseColor("#66BB6A"),
    val isSynced: Boolean = false,
    val doneDates: List<Int>,
)