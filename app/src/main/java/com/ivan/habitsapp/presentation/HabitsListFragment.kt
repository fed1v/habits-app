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
import com.ivan.habitsapp.presentation.adapter.HabitAdapter
import com.ivan.habitsapp.util.OnItemClickListener

class HabitsListFragment : Fragment() {

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private val habitItemClickListener = object : OnItemClickListener<Habit> {
        override fun onItemClicked(item: Habit) {
            openAddEditHabitFragment(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("HabitsListFragment onCreateView")
        binding = FragmentHabitsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        binding.buttonAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Add pressed", Toast.LENGTH_SHORT).show()
            openAddEditHabitFragment(null)
        }
    }

    private fun openAddEditHabitFragment(habit: Habit?) {
        parentFragmentManager
            .beginTransaction()
            .replace(
                R.id.mainActivityFragmentContainer,
                AddEditHabitFragment.newInstance(habit),
                "AddEditHabitFragment"
            )
            .addToBackStack("AddEditHabitFragment")
            .commit()
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener).apply {
            habits = HabitsProvider.habits
        }
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }

//
    //  override fun onCreate(savedInstanceState: Bundle?) {
    //      super.onCreate(savedInstanceState)
    //      binding = ActivityMainBinding.inflate(layoutInflater)
    //      setContentView(binding.root)
//
    //      initRecyclerView()
//
    //      binding.buttonAdd.setOnClickListener {
    //          startActivity(Intent(this, AddEditHabitActivity::class.java))
    //      }
    //  }
//
    //  private fun openAddEditActivityFragment(){
    //      supportFragmentManager
    //          .beginTransaction()
    //          .replace(R.id.)
    //  }
    //  private fun initRecyclerView() {
    //      habitAdapter = HabitAdapter(habitItemClickListener).apply {
    //          habits = HabitsProvider.habits
    //      }
//
    //      binding.recyclerviewHabitsList.adapter = habitAdapter
    //  }
}