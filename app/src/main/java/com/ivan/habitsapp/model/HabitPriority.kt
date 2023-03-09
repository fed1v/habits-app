package com.ivan.habitsapp.model

enum class HabitPriority {
    LOW, MEDIUM, HIGH;

    companion object {
        fun convertFromString(stringHabit: String): HabitPriority = when (stringHabit.lowercase()) {
            "low" -> LOW
            "medium" -> MEDIUM
            "high" -> HIGH
            else -> throw IllegalArgumentException()
        }
    }
}