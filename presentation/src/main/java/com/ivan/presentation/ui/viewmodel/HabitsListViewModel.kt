package com.ivan.presentation.ui.viewmodel

import androidx.lifecycle.*
import com.ivan.data.database.HabitsDao
import com.ivan.data.mappers.HabitEntityToHabitMapper
import com.ivan.domain.model.HabitOrder
import com.ivan.domain.repository.HabitsRepository
import com.ivan.domain.usecase.CompleteHabitUseCase
import com.ivan.domain.usecase.GetHabitsUseCase
import com.ivan.presentation.mappers.HabitPresentationToHabitMapper
import com.ivan.presentation.mappers.HabitToHabitPresentationMapper
import com.ivan.presentation.model.HabitPresentation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsListViewModel(
    private val filters: ((HabitPresentation) -> Boolean)?,
    private val habitsDao: HabitsDao,
    private val habitsRepository: HabitsRepository,
    private val getHabitsUseCase: GetHabitsUseCase,
    private val completeHabitUseCase: CompleteHabitUseCase
) : ViewModel() {

    private var habitsObserver = Observer<List<HabitPresentation>> {
        viewModelScope.launch(Dispatchers.IO) {
            _habitsListLiveData.postValue(
                filterHabits(
                    filters,
                    HabitOrder.NONE
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
        println("init")

        viewModelScope.launch(Dispatchers.IO) {
            //    _habitsFromDatabaseLivedata = habitsDao.getAllHabits()
            //        .map { list ->
            //            list.map {
            //                habitToHabitPresentationMapper.map(
            //                    habitEntityToHabitMapper.map(it)
            //                )
            //            }
            //        }


            launch(Dispatchers.Main) {
                _habitsFromDatabaseLivedata =
                    getHabitsUseCase.invoke()
                        .asLiveData(Dispatchers.IO)
                        .map { list ->
                            println("list in viewModel = $list")
                            list.map { it -> habitToHabitPresentationMapper.map(it) }
                        }

                _habitsFromDatabaseLivedata?.observeForever(habitsObserver)
            }
        }
    }

    fun showHabitsWithFilters(
        titleFilter: ((HabitPresentation) -> Boolean)?,
        priorityOrder: HabitOrder
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            _habitsListLiveData.setValue(filterHabits(titleFilter, priorityOrder))
        }
    }

    fun updateDataOnServer() {
        viewModelScope.launch(Dispatchers.IO) {
            habitsRepository.updateDataOnServer()
        }
    }

    fun completeHabit(habitPresentation: HabitPresentation) {
        viewModelScope.launch(Dispatchers.IO) {
            val habit = habitPresentationToHabitMapper.map(habitPresentation)
            completeHabitUseCase.invoke(habit)
        }
    }

    private fun filterHabits(
        titleFilter: ((HabitPresentation) -> Boolean)?,
        priorityOrder: HabitOrder
    ): List<HabitPresentation> {
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