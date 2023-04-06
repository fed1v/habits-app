package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.model.HabitOrder

class HabitsListViewModel(
    private val filters: ((Habit) -> Boolean)?
) : ViewModel() {

    private var _habitsListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    val habitsListLiveData: LiveData<List<Habit>>
        get() = _habitsListLiveData

    init {
        showHabitsWithFilters(filters, HabitOrder.NONE)
    }

    fun showHabitsWithFilters(
        titleFilter: ((Habit) -> Boolean)?,
        priorityOrder: HabitOrder
    ) {
        var filteredHabits = HabitsProvider.habits
            .filter { habit ->
                titleFilter?.invoke(habit) ?: true
            }.toMutableList()

        filteredHabits = when (priorityOrder) {
            HabitOrder.ASCENDING -> {
                filteredHabits
                    .sortedBy { it.priority }
                    .toMutableList()
            }
            HabitOrder.DESCENDING -> {
                filteredHabits
                    .sortedByDescending { it.priority }
                    .toMutableList()
            }
            HabitOrder.NONE -> filteredHabits
        }

        _habitsListLiveData.value = filteredHabits
    }
}