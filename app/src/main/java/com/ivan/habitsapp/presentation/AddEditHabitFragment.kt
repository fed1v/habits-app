package com.ivan.habitsapp.presentation

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
import androidx.room.Room
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentAddEditHabitBinding
import com.ivan.habitsapp.model.*
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.model.database.HabitsDatabase
import com.ivan.habitsapp.model.remote.AuthInterceptor
import com.ivan.habitsapp.model.remote.HabitsService
import com.ivan.habitsapp.model.remote.baseUrl
import com.ivan.habitsapp.model.repository.HabitsRepository
import com.ivan.habitsapp.presentation.viewmodel.AddEditHabitViewModel
import com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory.AddEditHabitViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddEditHabitFragment : Fragment() {

    companion object {
        private const val ARG_PARAM = "HABIT_PARAM"

        fun newInstance(habit: Habit?): AddEditHabitFragment {
            return AddEditHabitFragment().apply {
                val args = Bundle()
                args.putParcelable(ARG_PARAM, habit)
                arguments = args
            }
        }
    }

    private lateinit var habitsRepository: HabitsRepository

    private lateinit var habitsDatabase: HabitsDatabase
    private lateinit var habitsDao: HabitsDao

    private lateinit var authInterceptor: AuthInterceptor
    private lateinit var habitsService: HabitsService

    private lateinit var viewModel: AddEditHabitViewModel

    private lateinit var binding: FragmentAddEditHabitBinding
    private var habit: Habit? = null
    private var chosenColor: Int? = null

    private val token = "64f15871-0c48-4db0-9c3f-690ba8d8b6a7"

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

        initDatabase()
        initHabitsApi()
        initRepository()
        initViewModel()

        initButtonSaveClickListener()
        initColorClickListeners()
    }

    override fun onStart() {
        viewModel.showHabit(habit)
        super.onStart()
    }

    private fun initHabitsApi() {
        authInterceptor = AuthInterceptor(token)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        habitsService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HabitsService::class.java)
    }

    private fun initDatabase() {
        habitsDatabase = Room.databaseBuilder(
            requireContext().applicationContext,
            HabitsDatabase::class.java,
            "habits_database"
        )
            .allowMainThreadQueries()
            .build()

        habitsDao = habitsDatabase.getDao()
    }

    private fun initRepository() {
        habitsRepository = HabitsRepository(habitsService, habitsDao, token)
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(
            requireActivity(),
            AddEditHabitViewModelFactory(habitsRepository)
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
            val periodicityPeriod = Periods.valueOf(periodicityPeriodString.uppercase())

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

            val newHabit = Habit(
                title = binding.edittextTitle.text.toString(),
                description = binding.edittextDescription.text.toString(),
                priority = HabitPriority.valueOf(
                    binding.spinnerPriority.selectedItem.toString().uppercase()
                ),
                type = HabitType.values()[checkedId],
                periodicity = HabitPeriodicity(periodicityTimes, periodicityPeriod, periodsAmount),
                color = chosenColor ?: habit!!.color,
                date = date
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

    private fun initFields(habit: Habit?) {
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