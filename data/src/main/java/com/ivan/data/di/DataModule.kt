package com.ivan.data.di

import android.content.Context
import com.ivan.data.database.HabitsDao
import com.ivan.data.database.HabitsDatabase
import com.ivan.data.remote.HabitsService
import com.ivan.data.remote.baseUrl
import com.ivan.data.repository.HabitsRepositoryImpl
import com.ivan.domain.repository.HabitsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class DataModule(
    private val context: Context,
    private val token: String
) {

    @Provides
    fun provideHabitsService(): HabitsService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HabitsService::class.java)
    }

    @Provides
    fun provideHabitsDatabase(): HabitsDatabase {
        return HabitsDatabase.getInstance(context)
    }

    @Provides
    fun provideHabitsDao(database: HabitsDatabase): HabitsDao {
        return database.getDao()
    }

    @Provides
    fun provideHabitsRepository(
        habitsService: HabitsService,
        habitsDao: HabitsDao,
    ): HabitsRepository {
        return HabitsRepositoryImpl(
            habitsService = habitsService,
            habitsDao = habitsDao,
            token = token
        )
    }
}