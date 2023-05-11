package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.repository.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditHabitViewModel(
    private val habitsRepository: HabitsRepository
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
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.saveHabit(newHabit.copy(id = oldHabit?.id, uid = oldHabit?.uid ?: ""))
        }
    }
}