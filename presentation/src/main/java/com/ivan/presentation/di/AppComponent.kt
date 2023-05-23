package com.ivan.presentation.di

import com.ivan.data.di.DataModule
import com.ivan.presentation.ui.AddEditHabitFragment
import com.ivan.presentation.ui.HabitsListFragment
import dagger.Component

@Component(modules = [DataModule::class, PresentationModule::class])
interface AppComponent {

    fun injectHabitsListFragment(fragment: HabitsListFragment)

    fun injectAddEditHabitFragment(fragment: AddEditHabitFragment)
}