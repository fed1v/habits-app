package com.ivan.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [HabitEntity::class])
abstract class HabitsDatabase : RoomDatabase() {

    abstract fun getDao(): HabitsDao

    companion object {
        private var INSTANCE: HabitsDatabase? = null
        private const val DB_NAME = "habits_database"

        fun getInstance(context: Context): HabitsDatabase {
            if (INSTANCE == null) {
                synchronized(HabitsDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        HabitsDatabase::class.java,
                        DB_NAME
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}