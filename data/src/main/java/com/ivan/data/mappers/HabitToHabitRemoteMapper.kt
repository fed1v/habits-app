package com.ivan.data.mappers

import com.ivan.data.remote.HabitRemote
import com.ivan.domain.model.Habit

class HabitToHabitRemoteMapper {
    fun map(habit: Habit) = HabitRemote(
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