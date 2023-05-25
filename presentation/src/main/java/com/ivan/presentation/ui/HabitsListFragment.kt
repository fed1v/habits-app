package com.ivan.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ivan.domain.model.HabitOrder
import com.ivan.domain.model.HabitType
import com.ivan.presentation.R
import com.ivan.presentation.databinding.FragmentHabitsListBinding
import com.ivan.presentation.di.App
import com.ivan.presentation.model.HabitPresentation
import com.ivan.presentation.ui.adapter.HabitAdapter
import com.ivan.presentation.ui.viewmodel.HabitsListViewModel
import com.ivan.presentation.ui.viewmodel.viewmodel_factory.HabitsListViewModelFactory
import com.ivan.presentation.util.OnCompleteClickListener
import com.ivan.presentation.util.OnItemClickListener
import javax.inject.Inject

class HabitsListFragment : Fragment() {

    companion object {
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

    @Inject
    lateinit var viewModelFactory: HabitsListViewModelFactory
    private lateinit var viewModel: HabitsListViewModel

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var habits: MutableList<HabitPresentation>
    private var type: HabitType? = null

    private var filters: ((HabitPresentation) -> Boolean)? = null
    private var priorityOrder: HabitOrder? = null

    private val habitItemClickListener = object : OnItemClickListener<HabitPresentation> {
        override fun onItemClicked(item: HabitPresentation) {
            openAddEditHabitFragment(item)
        }
    }

    private val onCompleteClickListener = object : OnCompleteClickListener<HabitPresentation> {
        override fun onCompleteClicked(item: HabitPresentation) {
            viewModel.completeHabit(item)
            viewModel.showHabitsWithFilters(filters, HabitOrder.NONE)
            checkIfMaxAmountExceeded(item)
        }
    }

    private fun checkIfMaxAmountExceeded(item: HabitPresentation) {
        if (item.count >= item.frequency) {
            val message =
                if (item.type == HabitType.GOOD) "You are breathtaking!"
                else "Stop doing it"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        } else {
            val timesLeft = item.frequency - item.count

            val message =
                if (item.type == HabitType.GOOD) "You should do it $timesLeft times"
                else "$timesLeft times left"

            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

        injectDependencies()

        initFilters()
        initRecyclerView()
        initViewModel()

        updateDataOnServer()

        showBottomSheet()
        initSearchView()
        initRadioGroup()
        initResetButton()

        binding.buttonAdd.setOnClickListener {
            openAddEditHabitFragment(null)
        }
    }

    private fun injectDependencies() {
        val appComponent = (requireContext().applicationContext as App).appComponent
        appComponent.injectHabitsListFragment(this)
    }

    private fun updateDataOnServer() {
        viewModel.updateDataOnServer()
    }

    private fun initResetButton() {
        binding.fragmentHabitsListResetButton.setOnClickListener {
            resetFilters()
        }
    }

    private fun initRadioGroup() {
        binding.fragmentHabitsListOrderByPriorityAscending.setOnClickListener {
            priorityOrder = HabitOrder.ASCENDING
            viewModel.showHabitsWithFilters(
                { it.type == type },
                priorityOrder ?: HabitOrder.NONE
            )
        }

        binding.fragmentHabitsListOrderByPriorityDescending.setOnClickListener {
            priorityOrder = HabitOrder.DESCENDING
            viewModel.showHabitsWithFilters(
                { it.type == type },
                priorityOrder ?: HabitOrder.NONE
            )
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
            viewModelFactory
        )[HabitsListViewModel::class.java]

        viewModel.habitsListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                habits = it.toMutableList()
                habitAdapter.habits = habits
            }
        }
    }

    private fun openAddEditHabitFragment(habit: HabitPresentation?) {
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
        habitAdapter = HabitAdapter(habitItemClickListener, onCompleteClickListener)
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}