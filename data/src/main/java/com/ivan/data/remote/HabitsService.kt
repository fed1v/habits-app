package com.ivan.data.remote

import retrofit2.http.*

const val baseUrl = "https://droid-test-server.doubletapp.ru/api/"

interface HabitsService {

    @PUT("habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habit: HabitRemote,
    ): HabitResponseBody

    @GET("habit")
    suspend fun getHabits(): List<HabitRemote>
}