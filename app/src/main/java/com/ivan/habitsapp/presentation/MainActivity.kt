package com.ivan.habitsapp.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ivan.habitsapp.databinding.ActivityMainBinding
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.model.HabitPriority
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
            println("habit = $item")
            Toast.makeText(this@MainActivity, item.toString(), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Add habit", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AddEditHabitActivity::class.java))
        }
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener).apply {
            habits = habitsList.toMutableList()
        }

        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}

val habitsList = (1..15).map {
    Habit(
        title = "title$it",
        description = "description$it",
        periodicity = it,
        priority = if (it % 3 == 0) HabitPriority.LOW else if (it % 3 == 1) HabitPriority.MEDIUM else HabitPriority.HIGH,
        type = "type$it",
        color = it
    )
}