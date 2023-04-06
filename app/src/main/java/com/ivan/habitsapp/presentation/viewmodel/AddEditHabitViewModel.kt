package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.model.Habit

class AddEditHabitViewModel: ViewModel() {

    private var _habitLiveData: MutableLiveData<Habit?> = MutableLiveData<Habit?>()
    val habitLiveData: LiveData<Habit?>
        get() = _habitLiveData

    fun showHabit(habit: Habit?) {
        _habitLiveData.value = habit
    }

    fun saveHabit(oldHabit: Habit?, newHabit: Habit) {
        if (oldHabit == null) {
            HabitsProvider.habits.add(newHabit)
            return
        }

        val index = HabitsProvider.habits.indexOf(oldHabit)
        if (index >= 0 && index < HabitsProvider.habits.size) {
            HabitsProvider.habits[index] = newHabit
        }
    }
}