package com.ivan.habitsapp.presentation

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.ActivityAddEditHabitBinding
import com.ivan.habitsapp.model.*

class AddEditHabitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditHabitBinding
    private var habit: Habit? = null
    private var chosenColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        habit = intent.extras?.getParcelable(MainActivity.EXTRA_HABIT)

        initFields()

        binding.buttonSave.setOnClickListener {
            val radioButtonId = binding.radiogroupType.checkedRadioButtonId
            val radioButton = binding.radiogroupType.findViewById<RadioButton>(radioButtonId)
            val checkedId = binding.radiogroupType.indexOfChild(radioButton)

            if (checkedId == -1) {
                Toast.makeText(this, "Please select type of habit", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val periodicityTimes = binding.edittextPeriodicityAmount.text.toString().toInt()
            val periodicityPeriodString = binding.spinnerPeriodicityPeriod.selectedItem.toString()
            val periodicityPeriod = Periods.valueOf(periodicityPeriodString.uppercase())

            val periodsAmount = binding.edittextPeriodsAmount.text.toString().toInt()

            val newHabit = Habit(
                title = binding.edittextTitle.text.toString(),
                description = binding.edittextDescription.text.toString(),
                priority = HabitPriority.valueOf(
                    binding.spinnerPriority.selectedItem.toString().uppercase()
                ),
                type = HabitType.values()[checkedId],
                periodicity = HabitPeriodicity(periodicityTimes, periodicityPeriod, periodsAmount),
                color = chosenColor ?: habit!!.color
            )

            saveHabit(habit, newHabit)
            startActivity(Intent(this, MainActivity::class.java))
        }

        initColorClickListeners()

    }

    private fun initColorClickListeners() {
        binding.selectedColor.setOnClickListener {
            binding.scrollviewColors.isVisible = !binding.scrollviewColors.isVisible
        }

        binding.apply {
            val listener = OnClickListener { view ->
                chosenColor = (view.background as ColorDrawable).color
                selectedColor.background = ColorDrawable(chosenColor!!)
            }

            linearlayoutColors.children.forEach { it.setOnClickListener(listener) }
        }
    }

    private fun initFields() {
        if (habit != null) {
            binding.edittextTitle.setText(habit!!.title)
            binding.edittextDescription.setText(habit!!.description)
            binding.spinnerPriority.setSelection(habit!!.priority.ordinal)

            when (habit!!.type) {
                HabitType.BAD -> binding.radiogroupType.check(R.id.radiobutton_bad)
                HabitType.GOOD -> binding.radiogroupType.check(R.id.radiobutton_good)
            }

            binding.edittextPeriodicityAmount.setText(habit!!.periodicity.timesAmount.toString())
            binding.spinnerPeriodicityPeriod.setSelection(habit!!.periodicity.period.ordinal)
            binding.edittextPeriodsAmount.setText(habit!!.periodicity.periodsAmount.toString())
            binding.selectedColor.background = ColorDrawable(habit!!.color)
        } else {
            val color = Color.parseColor("#66BB6A")
            binding.selectedColor.background = ColorDrawable(color)
            chosenColor = color
        }
    }

    private fun saveHabit(oldHabit: Habit?, newHabit: Habit) {
        if (habit == null) {
            HabitsProvider.habits.add(newHabit)
            return
        }

        val index = HabitsProvider.habits.indexOf(oldHabit)
        HabitsProvider.habits[index] = newHabit
    }
}