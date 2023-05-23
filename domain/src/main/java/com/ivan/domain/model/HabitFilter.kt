package com.ivan.domain.model

data class HabitFilter(
    val title: String? = null,
    val types: List<HabitType>? = null
)