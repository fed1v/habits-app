package com.ivan.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.domain.model.DoneDates
import com.ivan.domain.model.HabitPeriodicity

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromHabitPeriodicity(periodicity: HabitPeriodicity): String {
        return gson.toJson(periodicity)
    }

    @TypeConverter
    fun toHabitPeriodicity(periodicityString: String): HabitPeriodicity {
        val objectType = object : TypeToken<HabitPeriodicity>() {}.type
        return gson.fromJson(periodicityString, objectType)
    }

    @TypeConverter
    fun fromDoneDates(doneDates: DoneDates): String {
       // return gson.toJson(doneDates)
        return "123"
    }

    @TypeConverter
    fun toDoneDates(doneDatesString: String): DoneDates {
        //val objectType = object : TypeToken<List<Int>>() {}.type
        //return gson.fromJson(doneDatesString, objectType)
        return DoneDates(listOf())
    }
}