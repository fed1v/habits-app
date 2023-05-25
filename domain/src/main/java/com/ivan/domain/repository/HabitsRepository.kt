package com.ivan.domain.repository

import com.ivan.domain.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    suspend fun getHabits(): Flow<List<Habit>>

    suspend fun saveHabit(habit: Habit)

    suspend fun updateDataOnServer()

    suspend fun completeHabit(habit: Habit)
}