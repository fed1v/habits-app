package com.ivan.habitsapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Habit::class])
abstract class HabitsDatabase: RoomDatabase() {

    abstract fun getDao() : HabitsDao
}