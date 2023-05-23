package com.ivan.presentation.model

import android.graphics.Color
import android.os.Parcelable
import com.ivan.domain.model.HabitPeriodicity
import com.ivan.domain.model.HabitPriority
import com.ivan.domain.model.HabitType
import kotlinx.parcelize.Parcelize

@Parcelize
data class HabitPresentation(
    val id: Int? = null,
    var uid: String = "",
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val date: Int,
    val periodicity: HabitPeriodicity,
    val color: Int = Color.parseColor("#66BB6A"),
    val isSynced: Boolean = false,
    val doneDates: List<Int>
) : Parcelable