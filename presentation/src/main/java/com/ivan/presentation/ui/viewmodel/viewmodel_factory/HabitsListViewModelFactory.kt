package com.ivan.presentation.ui.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.data.database.HabitsDao
import com.ivan.domain.repository.HabitsRepository
import com.ivan.presentation.ui.viewmodel.HabitsListViewModel
import com.ivan.presentation.model.HabitPresentation

class HabitsListViewModelFactory(
    private val filters: ((HabitPresentation) -> Boolean)?,
    private val habitsDao: HabitsDao,
    private val habitsRepository: HabitsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitsListViewModel(filters, habitsDao, habitsRepository) as T
    }
}