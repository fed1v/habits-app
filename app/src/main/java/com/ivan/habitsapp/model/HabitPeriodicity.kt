package com.ivan.habitsapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HabitPeriodicity(
    val timesAmount: Int,
    val period: Periods
) : Parcelable {
    override fun toString(): String {
        val timeString = if (timesAmount == 1) "time" else "times"
        return "$timesAmount $timeString / $period"
    }
}