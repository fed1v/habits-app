package com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.presentation.viewmodel.AddEditHabitViewModel

class AddEditHabitViewModelFactory(
    private val habitsDao: HabitsDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditHabitViewModel(habitsDao) as T
    }
}