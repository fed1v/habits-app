package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditHabitViewModel(
    private val habitsDao: HabitsDao
) : ViewModel() {

    private var _habitLiveData: MutableLiveData<Habit?> = MutableLiveData<Habit?>()
    val habitLiveData: LiveData<Habit?>
        get() = _habitLiveData

    fun showHabit(habit: Habit?) {
        viewModelScope.launch(Dispatchers.IO) {
            _habitLiveData.postValue(habit)
        }
    }

    fun saveHabit(
        oldHabit: Habit?,
        newHabit: Habit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsDao.insertHabit(newHabit.copy(id = oldHabit?.id))
        }
    }
}