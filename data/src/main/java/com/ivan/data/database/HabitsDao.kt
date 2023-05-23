package com.ivan.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HabitsDao {

    @Query("SELECT * FROM habits_table")
    fun getAllHabits(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM habits_table")
    suspend fun getHabitsList(): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)
}