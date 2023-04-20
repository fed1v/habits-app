package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao

class AddEditHabitViewModel(
    private val habitsDao: HabitsDao
) : ViewModel() {

    private var _habitLiveData: MutableLiveData<Habit?> = MutableLiveData<Habit?>()
    val habitLiveData: LiveData<Habit?>
        get() = _habitLiveData

    fun showHabit(habit: Habit?) {
        _habitLiveData.value = habit
    }

    fun saveHabit(
        oldHabit: Habit?,
        newHabit: Habit
    ) {
        habitsDao.insertHabit(newHabit.copy(id = oldHabit?.id))
    }
}