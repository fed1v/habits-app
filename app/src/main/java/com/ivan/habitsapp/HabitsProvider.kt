package com.ivan.habitsapp

import com.ivan.habitsapp.model.*

class HabitsProvider {
    companion object {
        val habits: MutableList<Habit> =// mutableListOf()
            (0..15).map {
                Habit(
                    title = "title$it",
                    description = "description$it",
                    periodicity = HabitPeriodicity(it, Periods.YEAR, it),
                    priority = if (it % 3 == 0) HabitPriority.LOW else if (it % 3 == 1) HabitPriority.MEDIUM else HabitPriority.HIGH,
                    type = if (it % 2 == 0) HabitType.BAD else HabitType.GOOD,
                )
            }.toMutableList()
    }
}