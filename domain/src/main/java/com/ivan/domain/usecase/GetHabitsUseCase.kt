package com.ivan.domain.usecase

import com.ivan.domain.model.Habit
import com.ivan.domain.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow

class GetHabitsUseCase(
    private val repository: HabitsRepository
) {

    suspend fun invoke(): Flow<List<Habit>> {
        return repository.getHabits()
    }
}