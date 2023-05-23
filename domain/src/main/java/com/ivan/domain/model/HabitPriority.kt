package com.ivan.domain.model

enum class HabitPriority(
    val type: String
) {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");

    override fun toString(): String {
        return type
    }
}