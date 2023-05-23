package com.ivan.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoneDates(
    val dates: List<Int>
) : Parcelable