package com.ivan.habitsapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.databinding.ActivityMainBinding
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.presentation.adapter.HabitAdapter
import com.ivan.habitsapp.util.OnItemClickListener

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_HABIT = "EXTRA_HABIT"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var habitAdapter: HabitAdapter

    private val habitItemClickListener = object : OnItemClickListener<Habit> {
        override fun onItemClicked(item: Habit) {
            Intent(this@MainActivity, AddEditHabitActivity::class.java).apply {
                putExtra(EXTRA_HABIT, item)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.buttonAdd.setOnClickListener {
            startActivity(Intent(this, AddEditHabitActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener).apply {
            habits = HabitsProvider.habits
        }

        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}