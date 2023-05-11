package com.ivan.habitsapp.model.mappers

import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.remote.HabitEntity

class HabitToHabitEntityMapper {
    fun map(habit: Habit) = HabitEntity(
        color = habit.color,
        count = -1,
        date = habit.date,
        description = habit.description,
        done_dates = listOf(0),
        frequency = habit.periodicity.timesAmount,
        priority = habit.priority.ordinal,
        title = habit.title,
        type = habit.type.ordinal,
        uid = if (habit.uid.isBlank()) null else habit.uid
    )
}