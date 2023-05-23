package com.ivan.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.domain.repository.HabitsRepository
import com.ivan.presentation.mappers.HabitPresentationToHabitMapper
import com.ivan.presentation.model.HabitPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEditHabitViewModel(
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private var _habitLiveData: MutableLiveData<HabitPresentation?> = MutableLiveData()
    val habitLiveData: LiveData<HabitPresentation?>
        get() = _habitLiveData

    private val habitPresentationToHabitMapper = HabitPresentationToHabitMapper()

    fun showHabit(habit: HabitPresentation?) {
        _habitLiveData.value = habit
    }

    fun saveHabit(
        oldHabit: HabitPresentation?,
        newHabit: HabitPresentation
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val habit = habitPresentationToHabitMapper.map(
                newHabit.copy(
                    id = oldHabit?.id,
                    uid = oldHabit?.uid ?: ""
                )
            )
            habitsRepository.saveHabit(habit)
        }
    }
}