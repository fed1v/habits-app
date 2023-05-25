package com.ivan.presentation.mappers

import com.ivan.domain.model.Habit
import com.ivan.domain.model.HabitPeriodicity
import com.ivan.domain.model.HabitPriority
import com.ivan.domain.model.HabitType
import com.ivan.presentation.model.HabitPresentation

class HabitPresentationToHabitMapper {

    fun map(habit: HabitPresentation) = Habit(
        id = habit.id,
        uid = habit.uid,
        title = habit.title,
        description = habit.description,
        priority = HabitPriority.values()[habit.priority.ordinal],
        type = HabitType.values()[habit.type.ordinal],
        date = habit.date,
        periodicity = HabitPeriodicity(
            timesAmount = habit.periodicity.timesAmount,
            period = habit.periodicity.period,
            periodsAmount = habit.periodicity.periodsAmount
        ),
        color = habit.color,
        isSynced = habit.isSynced,
        doneDates = habit.doneDates,
        count = habit.count,
        frequency = habit.frequency
    )
}