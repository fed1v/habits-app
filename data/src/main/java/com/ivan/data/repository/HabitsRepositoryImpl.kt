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
import kotlinx.coroutines.flow.flow
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

        try {
            val habitsRemote = habitsService.getHabits(token).map { habitRemote ->
                val habitLocal = habitsLocal.find { it.uid == habitRemote.uid }

                println("Search uid ${habitRemote.uid}")

                val habitEntity = HabitEntity(
                    id = habitLocal?.id,
                    uid = habitRemote.uid ?: "",
                    title = habitRemote.title,
                    description = habitRemote.description,
                    priority = HabitPriority.values()[habitRemote.priority],
                    type = HabitType.values()[habitRemote.type],
                    date = habitRemote.date,
                    periodicity = HabitPeriodicity(
                        timesAmount = -10,
                        period = Periods.DAY,
                        periodsAmount = -1
                    ),
                    color = habitRemote.color,
                    isSynced = true,
                    doneDates = DoneDates(habitRemote.done_dates),
                    frequency = habitRemote.frequency,
                    count = habitRemote.count
                )
                if (habitLocal == null) {
                    println("Local not found, insert: $habitEntity")
                    habitsDao.insertHabit(habitEntity)
                    return@map habitEntityToHabitMapper.map(habitEntity)
                }

                return@map habitEntityToHabitMapper.map(habitEntity)
            }

            return flow { emit(habitsRemote) }

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is HttpException) {
                Log.d(TAG, e.message())
                Log.d(TAG, e.response().toString())
            }
        }

        return flow { emit(habitsLocal.map { habitEntityToHabitMapper.map(it) }) }
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

    private suspend fun putToServer(habit: Habit) { // TODO edit
        val habitRemote = HabitToHabitRemoteMapper().map(habit)
        val response = habitsService.putHabit(token, habitRemote)

        val uid = response.uid

        val newHabit = habit.copy(id = habit.id, uid = uid, isSynced = true)

        Log.d(TAG, "Insert to database... $newHabit")
        val habitEntity = habitToHabitEntityMapper.map(newHabit)

        habitsDao.insertHabit(habitEntity)
        Log.d(TAG, "Inserted")
        Log.d(TAG, "Successfully saved: ${habitEntity}")
    }
}
