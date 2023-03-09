package com.ivan.habitsapp.model

enum class Periods(
    val type: String
) {
    HOUR("hour"), DAY("day"), WEEK("week"), MONTH("month"), YEAR("year");

    override fun toString(): String {
        return type
    }
}