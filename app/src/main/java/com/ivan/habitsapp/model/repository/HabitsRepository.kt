package com.ivan.habitsapp.model.repository

import android.util.Log
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.model.mappers.HabitToHabitEntityMapper
import com.ivan.habitsapp.model.remote.HabitsService
import retrofit2.HttpException

class HabitsRepository(
    private val habitsService: HabitsService,
    private val habitsDao: HabitsDao,
    private val token: String
) {

    private val TAG = "OkHttp"

    suspend fun saveHabit(habit: Habit) {
        Log.d(TAG, "saveHabit: $habit")
        val habitEntity = HabitToHabitEntityMapper().map(habit)
        try {
            val response = habitsService.putHabit(token, habitEntity)
            val uid = response.uid // добавить получение списка
            //
            val newHabit = habit.copy(id = habit.id, uid = uid, isSynced = true)
            habitsDao.insertHabit(newHabit)
            Log.d(TAG, "Successfully saved: ${newHabit}")
        } catch (e: Exception) {
            habitsDao.insertHabit(habit.copy(isSynced = false))
            e.printStackTrace()
            if (e is HttpException) {
                Log.d(TAG, e.message())
                Log.d(TAG, e.response().toString())
            }
        }
    }

    suspend fun updateDataOnServer() {
        val habitsFromDatabase = habitsDao.getHabitsList()

        habitsFromDatabase.forEach { habit ->
            if (!habit.isSynced) {
                saveHabit(habit)
            }
        }
    }
}
