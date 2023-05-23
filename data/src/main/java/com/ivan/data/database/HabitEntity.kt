package com.ivan.data.database

import android.graphics.Color
import android.os.Parcelable
import androidx.room.*
import com.ivan.data.Converters
import com.ivan.domain.model.DoneDates
import com.ivan.domain.model.HabitPeriodicity
import com.ivan.domain.model.HabitPriority
import com.ivan.domain.model.HabitType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "habits_table")
@TypeConverters(Converters::class)
data class HabitEntity(
    @PrimaryKey
    val id: Int? = null,
    var uid: String = "",
    val title: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val date: Int,
    val periodicity: HabitPeriodicity,
    val color: Int = Color.parseColor("#66BB6A"),
    val isSynced: Boolean,
    val doneDates: DoneDates,
    val frequency: Int,
    val count: Int
) : Parcelable