package com.ivan.domain.repository

import com.ivan.domain.model.Habit

interface HabitsRepository {

    suspend fun saveHabit(habit: Habit)

    suspend fun updateDataOnServer()
}