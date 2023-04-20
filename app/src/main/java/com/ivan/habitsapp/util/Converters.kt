package com.ivan.habitsapp.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.habitsapp.model.HabitPeriodicity

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromHabitPeriodicity(periodicity: HabitPeriodicity): String {
        return gson.toJson(periodicity)
    }

    @TypeConverter
    fun toHabitPeriodicity(periodicityString: String): HabitPeriodicity {
        val objectType = object : TypeToken<HabitPeriodicity>(){}.type
        return gson.fromJson(periodicityString, objectType)
    }
}