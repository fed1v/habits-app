package com.ivan.data.mappers

import com.ivan.data.remote.HabitRemote
import com.ivan.domain.model.Habit

class HabitToHabitRemoteMapper {
    fun map(habit: Habit) = HabitRemote(
        color = habit.color,
        count = habit.count,
        date = habit.date,
        description = habit.description,
        done_dates = habit.doneDates,
        frequency = habit.frequency,
        priority = habit.priority.ordinal,
        title = habit.title,
        type = habit.type.ordinal,
        uid = habit.uid.ifBlank { null },
    )
}