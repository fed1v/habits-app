package com.ivan.presentation.di

import com.ivan.data.database.HabitsDao
import com.ivan.domain.repository.HabitsRepository
import com.ivan.domain.usecase.CompleteHabitUseCase
import com.ivan.domain.usecase.GetHabitsUseCase
import com.ivan.presentation.ui.viewmodel.viewmodel_factory.AddEditHabitViewModelFactory
import com.ivan.presentation.ui.viewmodel.viewmodel_factory.HabitsListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideAddEditHabitViewModelFactory(
        repository: HabitsRepository
    ): AddEditHabitViewModelFactory {
        return AddEditHabitViewModelFactory(repository)
    }

    @Provides
    fun provideHabitsListViewModelFactory(
        dao: HabitsDao,
        repository: HabitsRepository,
    ): HabitsListViewModelFactory {
        return HabitsListViewModelFactory(
            filters = { true },
            habitsDao = dao,
            habitsRepository = repository,
            getHabitsUseCase = GetHabitsUseCase(repository),
            completeHabitUseCase = CompleteHabitUseCase(repository)
        )
    }
}