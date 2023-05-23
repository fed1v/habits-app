package com.ivan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HabitPeriodicity(
    val timesAmount: Int,
    val period: Periods,
    val periodsAmount: Int
) : Parcelable {
    override fun toString(): String {
        val timeString = if (timesAmount == 1) "time" else "times"
        return "$timesAmount $timeString / $periodsAmount $period"
    }
}