package com.ivan.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM habits_table")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits_table")
    suspend fun getHabitsList(): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)
}