package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.model.HabitFilter

class HabitsListViewModel(
    private val filters: HabitFilter?
) : ViewModel() {

    private var _habitsListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    val habitsListLiveData: LiveData<List<Habit>>
        get() = _habitsListLiveData

    init {
        showHabitsWithFilters()
    }

    private fun showHabitsWithFilters() {
        var filteredHabits = HabitsProvider.habits
        filters?.let {
            filteredHabits = filteredHabits.filter {
                (filters.title == null || it.title.contains(filters.title))
                        && (filters.types == null || filters.types.contains(it.type))
            }.toMutableList()
        }
        _habitsListLiveData.value = filteredHabits
    }
}