package com.ivan.domain.usecase

import com.ivan.domain.model.Habit
import com.ivan.domain.repository.HabitsRepository

class SaveHabitUseCase(
    private val repository: HabitsRepository
) {

    suspend fun invoke(habit: Habit) {
        repository.saveHabit(habit)
    }
}