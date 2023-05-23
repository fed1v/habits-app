package com.ivan.presentation.ui.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.domain.repository.HabitsRepository
import com.ivan.presentation.ui.viewmodel.AddEditHabitViewModel

class AddEditHabitViewModelFactory(
    private val habitsRepository: HabitsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditHabitViewModel(habitsRepository) as T
    }
}