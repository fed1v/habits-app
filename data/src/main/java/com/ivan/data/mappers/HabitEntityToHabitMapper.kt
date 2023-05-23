package com.ivan.data.mappers

import com.ivan.data.database.HabitEntity
import com.ivan.domain.model.Habit

class HabitEntityToHabitMapper {

    fun map(habit: HabitEntity) = Habit(
        id = habit.id,
        color = habit.color,
        date = habit.date,
        description = habit.description,
        doneDates = habit.doneDates.dates,
        priority = habit.priority,
        title = habit.title,
        type = habit.type,
        uid = habit.uid.ifBlank { "" },
        periodicity = habit.periodicity,
        isSynced = habit.isSynced
    )
}