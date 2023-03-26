package com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.habitsapp.model.HabitFilter
import com.ivan.habitsapp.presentation.viewmodel.HabitsListViewModel

class HabitsListViewModelFactory(
    private val filters: HabitFilter?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitsListViewModel(filters) as T
    }
}