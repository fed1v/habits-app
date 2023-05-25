package com.ivan.presentation.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ivan.domain.model.HabitPeriodicity
import com.ivan.domain.model.HabitPriority
import com.ivan.domain.model.HabitType
import com.ivan.domain.model.Periods
import com.ivan.presentation.R
import com.ivan.presentation.databinding.FragmentAddEditHabitBinding
import com.ivan.presentation.di.App
import com.ivan.presentation.model.HabitPresentation
import com.ivan.presentation.ui.viewmodel.AddEditHabitViewModel
import com.ivan.presentation.ui.viewmodel.viewmodel_factory.AddEditHabitViewModelFactory
import javax.inject.Inject

class AddEditHabitFragment : Fragment() {

    companion object {
        const val ARG_PARAM = "HABIT_PARAM"
    }

    @Inject
    lateinit var viewModelFactory: AddEditHabitViewModelFactory

    private lateinit var viewModel: AddEditHabitViewModel

    private lateinit var binding: FragmentAddEditHabitBinding
    private var habit: HabitPresentation? = null
    private var chosenColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = it.getParcelable(ARG_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectDependencies()

        initViewModel()

        initButtonSaveClickListener()
        initColorClickListeners()
    }

    private fun injectDependencies() {
        val appComponent = (requireContext().applicationContext as App).appComponent
        appComponent.injectAddEditHabitFragment(this)
    }

    override fun onStart() {
        viewModel.showHabit(habit)
        super.onStart()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AddEditHabitViewModel::class.java]

        viewModel.habitLiveData.observe(viewLifecycleOwner) {
            initFields(it)
        }
    }

    private fun initButtonSaveClickListener() {
        binding.buttonSave.setOnClickListener {
            val radioButtonId = binding.radiogroupType.checkedRadioButtonId
            val radioButton = binding.radiogroupType.findViewById<RadioButton>(radioButtonId)
            val checkedId = binding.radiogroupType.indexOfChild(radioButton)

            if (checkedId == -1) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_type_of_habit),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val periodicityTimes = binding.edittextPeriodicityAmount.text.toString().toInt()
            val periodicityPeriodString = binding.spinnerPeriodicityPeriod.selectedItem.toString()
            val periodicityPeriod =
                Periods.valueOf(periodicityPeriodString.uppercase())

            val periodsAmount = binding.edittextPeriodsAmount.text.toString().toInt()

            if (binding.edittextTitle.text.toString().isBlank()) {
                Toast.makeText(requireContext(), "Please enter Title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.edittextDescription.text.toString().isBlank()) {
                Toast.makeText(requireContext(), "Please enter Description", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val date = (System.currentTimeMillis() / 1000).toInt()

            val newHabit = HabitPresentation(
                title = binding.edittextTitle.text.toString(),
                description = binding.edittextDescription.text.toString(),
                priority = HabitPriority.valueOf(
                    binding.spinnerPriority.selectedItem.toString().uppercase()
                ),
                type = HabitType.values()[checkedId],
                periodicity = HabitPeriodicity(
                    periodicityTimes,
                    periodicityPeriod,
                    periodsAmount
                ),
                color = chosenColor ?: habit!!.color,
                date = date,
                doneDates = habit?.doneDates ?: listOf(),
                count = /*habit?.count ?:*/ periodicityTimes,
                frequency = /*habit?.frequency ?:*/ periodsAmount
            )

            viewModel.saveHabit(habit, newHabit)
            openNavHost()
        }
    }

    private fun openNavHost() {
        (requireActivity() as MainActivity)
            .navController
            .navigate(R.id.action_addEditHabitFragment_to_habitsViewPagerFragment)
    }

    private fun initColorClickListeners() {
        binding.selectedColor.setOnClickListener {
            binding.scrollviewColors.isVisible = !binding.scrollviewColors.isVisible
        }

        binding.apply {
            val listener = View.OnClickListener { view ->
                chosenColor = (view.background as ColorDrawable).color
                selectedColor.background = ColorDrawable(chosenColor!!)
            }

            linearlayoutColors.children.forEach { it.setOnClickListener(listener) }
        }
    }

    private fun initFields(habit: HabitPresentation?) {
        if (habit != null) {
            binding.edittextTitle.setText(habit.title)
            binding.edittextDescription.setText(habit.description)
            binding.spinnerPriority.setSelection(habit.priority.ordinal)

            when (habit.type) {
                HabitType.BAD -> binding.radiogroupType.check(R.id.radiobutton_bad)
                HabitType.GOOD -> binding.radiogroupType.check(R.id.radiobutton_good)
            }

            binding.edittextPeriodicityAmount.setText(habit.periodicity.timesAmount.toString())
            binding.spinnerPeriodicityPeriod.setSelection(habit.periodicity.period.ordinal)
            binding.edittextPeriodsAmount.setText(habit.periodicity.periodsAmount.toString())
            binding.selectedColor.background = ColorDrawable(habit.color)
        } else {
            val color = ContextCompat.getColor(requireContext(), R.color.default_green)
            binding.selectedColor.background = ColorDrawable(color)
            chosenColor = color
        }
    }
}