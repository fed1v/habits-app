package com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.presentation.viewmodel.HabitsListViewModel

class HabitsListViewModelFactory(
    private val filters: ((Habit) -> Boolean)?,
    private val habitsDao: HabitsDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitsListViewModel(filters, habitsDao) as T
    }
}