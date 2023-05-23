package com.ivan.domain.model

enum class Periods(
    val type: String
) {
    HOUR("hour"), DAY("day"), WEEK("week"), MONTH("month"), YEAR("year");

    override fun toString(): String {
        return type
    }
}