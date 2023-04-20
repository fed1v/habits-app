package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.ivan.habitsapp.model.HabitOrder
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao

class HabitsListViewModel(
    private val filters: ((Habit) -> Boolean)?,
    private val habitsDao: HabitsDao
) : ViewModel() {

    private var habitsObserver = Observer<List<Habit>> {
        _habitsListLiveData.value = filterHabits(filters, HabitOrder.NONE)
    }

    private var _habitsListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    val habitsListLiveData: LiveData<List<Habit>>
        get() = _habitsListLiveData

    private var _habitsFromDatabaseLivedata: LiveData<List<Habit>>? = null

    init {
        _habitsFromDatabaseLivedata = habitsDao.getAllHabits()
        _habitsFromDatabaseLivedata?.observeForever(habitsObserver)
    }

    fun showHabitsWithFilters(
        titleFilter: ((Habit) -> Boolean)?,
        priorityOrder: HabitOrder
    ) {
        _habitsListLiveData.value = filterHabits(titleFilter, priorityOrder)
    }

    private fun filterHabits(
        titleFilter: ((Habit) -> Boolean)?,
        priorityOrder: HabitOrder
    ): List<Habit> {
        var filteredHabits = _habitsFromDatabaseLivedata?.value
            ?.filter { habit ->
                titleFilter?.invoke(habit) ?: true
            }
            ?.toMutableList() ?: listOf()

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

        return filteredHabits
    }

    override fun onCleared() {
        _habitsFromDatabaseLivedata?.removeObserver(habitsObserver)
        super.onCleared()
    }
}