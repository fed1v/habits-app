package com.ivan.habitsapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivan.habitsapp.databinding.ActivityAddEditHabitBinding
import com.ivan.habitsapp.model.Habit

class AddEditHabitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditHabitBinding
    private var habit: Habit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.extras?.getParcelable(MainActivity.EXTRA_HABIT)

        habit?.title.let {
            binding.edittextTitle.setText(it)
        }


    }
}