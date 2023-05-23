package com.ivan.presentation.di

import android.app.Application
import com.ivan.data.di.DataModule

class App : Application() {

    lateinit var appComponent: AppComponent
    private val token: String = "64f15871-0c48-4db0-9c3f-690ba8d8b6a7"

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .dataModule(DataModule(this, token))
            .build()
    }
}