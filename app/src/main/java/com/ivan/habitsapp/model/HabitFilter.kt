package com.ivan.habitsapp.model

data class HabitFilter(
    val title: String? = null,
    val types: List<HabitType>? = null
)