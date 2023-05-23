package com.ivan.habitsapp.presentation.viewmodel

import androidx.lifecycle.*
import com.ivan.data.mappers.HabitEntityToHabitMapper
import com.ivan.domain.repository.HabitsRepository
import com.ivan.presentation.mappers.HabitPresentationToHabitMapper
import com.ivan.presentation.mappers.HabitToHabitPresentationMapper
import com.ivan.presentation.model.HabitPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsListViewModel(
    private val filters: ((HabitPresentation) -> Boolean)?,
    private val habitsDao: com.ivan.data.database.HabitsDao,
    private val habitsRepository: HabitsRepository
) : ViewModel() {

    private var habitsObserver = Observer<List<HabitPresentation>> {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsListLiveData.postValue(
                filterHabits(
                    filters,
                    com.ivan.domain.model.HabitOrder.NONE
                )
            )
        }
    }

    private var _habitsListLiveData: MutableLiveData<List<HabitPresentation>> = MutableLiveData()
    val habitsListLiveData: LiveData<List<HabitPresentation>>
        get() = _habitsListLiveData

    private var _habitsFromDatabaseLivedata: LiveData<List<HabitPresentation>>? = null

    private val habitPresentationToHabitMapper = HabitPresentationToHabitMapper()
    private val habitToHabitPresentationMapper = HabitToHabitPresentationMapper()
    private val habitEntityToHabitMapper = HabitEntityToHabitMapper()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsFromDatabaseLivedata = habitsDao.getAllHabits()
                .map { list ->
                    list.map {
                        habitToHabitPresentationMapper.map(
                            habitEntityToHabitMapper.map(it)
                        )
                    }
                }

            launch(Dispatchers.Main) {
                _habitsFromDatabaseLivedata?.observeForever(habitsObserver)
            }
        }
    }

    fun showHabitsWithFilters(
        titleFilter: ((HabitPresentation) -> Boolean)?,
        priorityOrder: com.ivan.domain.model.HabitOrder
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsListLiveData.postValue(filterHabits(titleFilter, priorityOrder))
        }
    }

    fun updateDataOnServer() {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.updateDataOnServer()
        }
    }

    private fun filterHabits(
        titleFilter: ((HabitPresentation) -> Boolean)?,
        priorityOrder: com.ivan.domain.model.HabitOrder
    ): List<HabitPresentation> {
        var filteredHabits = _habitsFromDatabaseLivedata?.value
            ?.filter { habit ->
                titleFilter?.invoke(habit) ?: true
            }
            ?.toMutableList() ?: listOf()

        filteredHabits = when (priorityOrder) {
            com.ivan.domain.model.HabitOrder.ASCENDING -> {
                filteredHabits
                    .sortedBy { it.priority }
                    .toMutableList()
            }
            com.ivan.domain.model.HabitOrder.DESCENDING -> {
                filteredHabits
                    .sortedByDescending { it.priority }
                    .toMutableList()
            }
            com.ivan.domain.model.HabitOrder.NONE -> filteredHabits
        }

        return filteredHabits
    }

    override fun onCleared() {
        _habitsFromDatabaseLivedata?.removeObserver(habitsObserver)
        super.onCleared()
    }
}