package com.ivan.data.repository

import android.util.Log
import com.ivan.data.database.HabitsDao
import com.ivan.data.mappers.HabitEntityToHabitMapper
import com.ivan.data.mappers.HabitToHabitEntityMapper
import com.ivan.data.mappers.HabitToHabitRemoteMapper
import com.ivan.data.remote.HabitsService
import com.ivan.domain.model.Habit
import com.ivan.domain.repository.HabitsRepository
import retrofit2.HttpException

class HabitsRepositoryImpl(
    private val habitsService: HabitsService,
    private val habitsDao: HabitsDao,
    private val token: String
) : HabitsRepository {

    private val TAG = "OkHttp"

    private val habitToHabitEntityMapper = HabitToHabitEntityMapper()
    private val habitEntityToHabitMapper = HabitEntityToHabitMapper()

    override suspend fun saveHabit(habit: Habit) {
        Log.d(TAG, "        saveHabit: $habit")
        try {
            putToServer(habit)
        } catch (e: Exception) {
            val habitEntity = habitToHabitEntityMapper.map(habit.copy(isSynced = false))
            habitsDao.insertHabit(habitEntity)
            e.printStackTrace()
            if (e is HttpException) {
                Log.d(TAG, e.message())
                Log.d(TAG, e.response().toString())
            }
        }
    }

    suspend fun syncData(){
        val habitsRemote = habitsService.getHabits(token)
        val habitsLocal = habitsDao.getHabitsList()

        TODO()

    }
    override suspend fun updateDataOnServer() {
        val habitsFromDatabase = habitsDao.getHabitsList()

        habitsFromDatabase.forEach { habitEntity ->
            if (!habitEntity.isSynced) {
                val habit = habitEntityToHabitMapper.map(habitEntity)
                saveHabit(habit)
            }
        }
    }

    private suspend fun putToServer(habit: Habit) { // TODO edit
        val habitRemote = HabitToHabitRemoteMapper().map(habit)
        val response = habitsService.putHabit(token, habitRemote)

        val uid = response.uid // добавить получение списка

        val newHabit = habit.copy(id = habit.id, uid = uid, isSynced = true)
        val habitEntity = habitToHabitEntityMapper.map(newHabit)

        habitsDao.insertHabit(habitEntity)
        Log.d(TAG, "Successfully saved: ${habitEntity}")
    }
}
