package com.ivan.data.mappers

import com.ivan.data.database.HabitEntity
import com.ivan.domain.model.DoneDates
import com.ivan.domain.model.Habit

class HabitToHabitEntityMapper {

    fun map(habit: Habit) = HabitEntity(
        id = habit.id,
        color = habit.color,
        count = habit.count,
        date = habit.date,
        description = habit.description,
        doneDates = DoneDates(habit.doneDates),
        frequency = habit.frequency,
        priority = habit.priority,
        title = habit.title,
        type = habit.type,
        uid = habit.uid.ifBlank { "" },
        periodicity = habit.periodicity,
        isSynced = habit.isSynced
    )
}