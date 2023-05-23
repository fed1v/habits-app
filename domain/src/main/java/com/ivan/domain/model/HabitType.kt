package com.ivan.domain.model

enum class HabitType(
    val type: String
) {
    BAD("Bad"), GOOD("Good");

    override fun toString(): String {
        return type
    }
}