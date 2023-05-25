package com.ivan.data.repository

import android.util.Log
import com.ivan.data.database.HabitEntity
import com.ivan.data.database.HabitsDao
import com.ivan.data.mappers.HabitEntityToHabitMapper
import com.ivan.data.mappers.HabitToHabitEntityMapper
import com.ivan.data.mappers.HabitToHabitRemoteMapper
import com.ivan.data.remote.HabitsService
import com.ivan.domain.model.*
import com.ivan.domain.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class HabitsRepositoryImpl(
    private val habitsService: HabitsService,
    private val habitsDao: HabitsDao,
    private val token: String
) : HabitsRepository {

    private val TAG = "OkHttp"

    private val habitToHabitEntityMapper = HabitToHabitEntityMapper()
    private val habitEntityToHabitMapper = HabitEntityToHabitMapper()

    override suspend fun getHabits(): Flow<List<Habit>> {
        val habitsLocal = habitsDao.getHabitsList()

        return habitsDao.getAllHabits().map {
            try {
                return@map mapRemoteToLocal(it)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is HttpException) {
                    Log.d(TAG, e.message())
                    Log.d(TAG, e.response().toString())
                }

                return@map habitsLocal.map { habitEntity ->
                    habitEntityToHabitMapper.map(habitEntity)
                }
            }
        }
    }

    private suspend fun mapRemoteToLocal(localHabits: List<HabitEntity>): List<Habit> {
        val habitsRemote = habitsService.getHabits(token).map { habitRemote ->
            val habitLocal = localHabits.find { it.uid == habitRemote.uid }

            val habitEntity = HabitEntity(
                id = habitLocal?.id,
                uid = habitRemote.uid ?: "",
                title = habitRemote.title,
                description = habitRemote.description,
                priority = HabitPriority.values()[habitRemote.priority],
                type = HabitType.values()[habitRemote.type],
                date = habitRemote.date,
                periodicity = habitLocal?.periodicity ?: HabitPeriodicity(
                    period = Periods.DAY,
                    periodsAmount = 1,
                    timesAmount = 1
                ),
                color = habitRemote.color,
                isSynced = true,
                doneDates = DoneDates(habitRemote.done_dates),
                frequency = habitRemote.frequency,
                count = habitRemote.count
            )
            if (habitLocal == null) {
                habitsDao.insertHabit(habitEntity)
                return@map habitEntityToHabitMapper.map(habitEntity)
            }

            return@map habitEntityToHabitMapper.map(habitEntity)
        }

        return habitsRemote
    }

    override suspend fun saveHabit(habit: Habit) {
        Log.d(TAG, "  saveHabit: $habit")
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

    override suspend fun updateDataOnServer() {
        val habitsFromDatabase = habitsDao.getHabitsList()

        habitsFromDatabase.forEach { habitEntity ->
            if (!habitEntity.isSynced) {
                val habit = habitEntityToHabitMapper.map(habitEntity)
                try {
                    putToServer(habit)
                } catch (_: Exception) {
                }
            }
        }
    }

    override suspend fun completeHabit(habit: Habit) {
        saveHabit(
            habit.copy(
                count = habit.count + 1,
                date = (System.currentTimeMillis() / 1000).toInt(),
                periodicity = habit.periodicity.copy(timesAmount = habit.periodicity.timesAmount + 1)
            )
        )
    }

    private suspend fun putToServer(habit: Habit) {
        val habitRemote = HabitToHabitRemoteMapper().map(habit)
        val response = habitsService.putHabit(token, habitRemote)

        val uid = response.uid

        val newHabit = habit.copy(id = habit.id, uid = uid, isSynced = true)

        val habitEntity = habitToHabitEntityMapper.map(newHabit)

        habitsDao.insertHabit(habitEntity)
        Log.d(TAG, "Successfully saved: ${habitEntity}")
    }
}
