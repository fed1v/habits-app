package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentHabitsListBinding
import com.ivan.habitsapp.model.database.Habit
import com.ivan.habitsapp.model.HabitOrder
import com.ivan.habitsapp.model.HabitType
import com.ivan.habitsapp.model.database.HabitsDao
import com.ivan.habitsapp.model.database.HabitsDatabase
import com.ivan.habitsapp.presentation.adapter.HabitAdapter
import com.ivan.habitsapp.presentation.viewmodel.HabitsListViewModel
import com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory.HabitsListViewModelFactory
import com.ivan.habitsapp.util.OnItemClickListener

class HabitsListFragment : Fragment() {

    companion object {
        const val HABITS_LIST_FRAGMENT_TAG = "HabitsListFragment"
        private const val TYPE_ARG_PARAM = "TYPE_PARAM"
        private const val HABIT_ARG_PARAM = "HABIT_PARAM"

        fun newInstance(type: HabitType?): HabitsListFragment {
            return HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_ARG_PARAM, type?.name)
                }
            }
        }
    }

    private lateinit var habitsDatabase: HabitsDatabase
    private lateinit var habitsDao: HabitsDao

    private lateinit var viewModel: HabitsListViewModel

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var habits: MutableList<Habit>
    private var type: HabitType? = null

    private var filters: ((Habit) -> Boolean)? = null
    private var priorityOrder: HabitOrder? = null

    private val habitItemClickListener = object : OnItemClickListener<Habit> {
        override fun onItemClicked(item: Habit) {
            openAddEditHabitFragment(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString((TYPE_ARG_PARAM))?.let { typeString ->
                HabitType.valueOf(typeString)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFilters()
        initRecyclerView()
        initDatabase()
        initViewModel()

        showBottomSheet()
        initSearchView()
        initRadioGroup()
        initResetButton()

        binding.buttonAdd.setOnClickListener {
            openAddEditHabitFragment(null)
        }
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

    private fun initResetButton() {
        binding.fragmentHabitsListResetButton.setOnClickListener {
            resetFilters()
        }
    }

    private fun initRadioGroup() {
        binding.fragmentHabitsListOrderByPriorityAscending.setOnClickListener {
            priorityOrder = HabitOrder.ASCENDING
            viewModel.showHabitsWithFilters({ it.type == type }, priorityOrder ?: HabitOrder.NONE)
        }

        binding.fragmentHabitsListOrderByPriorityDescending.setOnClickListener {
            priorityOrder = HabitOrder.DESCENDING
            viewModel.showHabitsWithFilters({ it.type == type }, priorityOrder ?: HabitOrder.NONE)
        }
    }

    private fun resetFilters() {
        viewModel.showHabitsWithFilters({ it.type == type }, HabitOrder.NONE)
        binding.fragmentHabitsListOrderByPriorityRadioGroup.clearCheck()
    }

    private fun initSearchView() {
        binding.fragmentHabitsListSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                filterHabits(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean = true
        })
    }

    private fun showBottomSheet() {
        BottomSheetBehavior.from(binding.fragmentHabitsListBottomSheet)
    }

    private fun initFilters() {
        filters = { habit ->
            type?.let { type ->
                habit.type == type
            } ?: true
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            HabitsListViewModelFactory(filters, habitsDao)
        )[HabitsListViewModel::class.java]

        viewModel.habitsListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                habits = it.toMutableList()
                habitAdapter.habits = habits
            }
        }
    }

    private fun openAddEditHabitFragment(habit: Habit?) {
        val bundle = Bundle().apply {
            putParcelable(HABIT_ARG_PARAM, habit)
        }

        (requireActivity() as MainActivity)
            .navController
            .navigate(R.id.action_habitsViewPagerFragment_to_addEditHabitFragment, bundle)
    }

    private fun filterHabits(textQuery: String) {
        viewModel.showHabitsWithFilters(
            titleFilter = {
                it.title.contains(textQuery) && it.type == type
            },
            priorityOrder = priorityOrder ?: HabitOrder.NONE
        )
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener)
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}