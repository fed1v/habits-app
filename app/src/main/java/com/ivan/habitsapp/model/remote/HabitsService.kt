package com.ivan.habitsapp.model.remote

import retrofit2.http.*

const val baseUrl = "https://droid-test-server.doubletapp.ru/api/"

interface HabitsService {

    @PUT("habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habit: HabitEntity,
    ): HabitResponseBody

    @GET("habit")
    suspend fun getHabits(): List<HabitEntity>
}