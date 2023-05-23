package com.ivan.presentation.di

import com.ivan.domain.repository.HabitsRepository
import com.ivan.presentation.ui.viewmodel.viewmodel_factory.AddEditHabitViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideAddEditHabitViewModelFactory(
        repository: HabitsRepository
    ) : AddEditHabitViewModelFactory {
        return AddEditHabitViewModelFactory(repository)
    }
}