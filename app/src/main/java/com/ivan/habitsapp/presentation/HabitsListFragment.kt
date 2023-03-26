package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentHabitsListBinding
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.model.HabitFilter
import com.ivan.habitsapp.model.HabitType
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

    private lateinit var viewModel: HabitsListViewModel

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var habits: MutableList<Habit>
    private var type: HabitType? = null

    private var filters: HabitFilter? = null

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
        initViewModel()
        binding.buttonAdd.setOnClickListener {
            openAddEditHabitFragment(null)
        }
    }

    private fun initFilters() {
        filters = HabitFilter(
            types = if (type == null) null else listOf(type!!)
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            HabitsListViewModelFactory(filters)
        )[HabitsListViewModel::class.java]

        viewModel.habitsListLiveData.observe(viewLifecycleOwner) {
            habits = it.toMutableList()
            habitAdapter.habits = habits
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

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener)
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}