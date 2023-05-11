package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.*
import com.ivan.habitsapp.model.HabitOrder
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.model.repository.HabitsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsListViewModel(
    private val filters: ((Habit) -> Boolean)?,
    private val habitsDao: HabitsDao,
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private var habitsObserver = Observer<List<Habit>> {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsListLiveData.postValue(filterHabits(filters, HabitOrder.NONE))
        }
    }

    private var _habitsListLiveData: MutableLiveData<List<Habit>> = MutableLiveData()
    val habitsListLiveData: LiveData<List<Habit>>
        get() = _habitsListLiveData

    private var _habitsFromDatabaseLivedata: LiveData<List<Habit>>? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsFromDatabaseLivedata = habitsDao.getAllHabits()

            launch(Dispatchers.Main) {
                _habitsFromDatabaseLivedata?.observeForever(habitsObserver)
            }
        }
    }

    fun showHabitsWithFilters(
        titleFilter: ((Habit) -> Boolean)?,
        priorityOrder: HabitOrder
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsListLiveData.postValue(filterHabits(titleFilter, priorityOrder))
        }
    }

    fun updateDataOnServer(){
        viewModelScope.launch(Dispatchers.IO){
            habitsRepository.updateDataOnServer()
        }
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