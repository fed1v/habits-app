package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ivan.habitsapp.HabitsProvider
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentHabitsListBinding
import com.ivan.habitsapp.model.Habit
import com.ivan.habitsapp.model.HabitType
import com.ivan.habitsapp.presentation.adapter.HabitAdapter
import com.ivan.habitsapp.util.OnItemClickListener

class HabitsListFragment : Fragment() {

    companion object {
        const val HABITS_LIST_FRAGMENT_TAG = "HabitsListFragment"
        private const val ARG_PARAM = "HABIT_PARAM"

        fun newInstance(type: HabitType?): HabitsListFragment {
            return HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM, type?.name)
                }
            }
        }
    }

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var habits: MutableList<Habit>
    private var type: HabitType? = null

    private val habitItemClickListener = object : OnItemClickListener<Habit> {
        override fun onItemClicked(item: Habit) {
            openAddEditHabitFragment(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString((ARG_PARAM))?.let { typeString -> HabitType.valueOf(typeString) }
            Toast.makeText(requireContext(), "Type: $type", Toast.LENGTH_SHORT).show()
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

        habits = HabitsProvider.habits
        type?.let { habitType ->
            habits = habits
                .filter { habit -> habit.type == habitType }
                .toMutableList()
        }

        initRecyclerView()
        binding.buttonAdd.setOnClickListener {
            openAddEditHabitFragment(null)
        }
    }

    private fun openAddEditHabitFragment(habit: Habit?) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainActivityFragmentContainer,
                AddEditHabitFragment.newInstance(habit),
                HABITS_LIST_FRAGMENT_TAG
            )
            .addToBackStack(HABITS_LIST_FRAGMENT_TAG)
            .commit()
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener).apply {
            habits = this@HabitsListFragment.habits
        }
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}