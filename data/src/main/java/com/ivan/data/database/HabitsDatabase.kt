package com.ivan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [HabitEntity::class])
abstract class HabitsDatabase: RoomDatabase() {

    abstract fun getDao() : HabitsDao
}