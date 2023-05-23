package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.ivan.data.repository.HabitsRepositoryImpl
import com.ivan.domain.repository.HabitsRepository
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentHabitsListBinding
import com.ivan.habitsapp.presentation.adapter.HabitAdapter
import com.ivan.habitsapp.presentation.viewmodel.HabitsListViewModel
import com.ivan.habitsapp.presentation.viewmodel.viewmodel_factory.HabitsListViewModelFactory
import com.ivan.habitsapp.util.OnItemClickListener
import com.ivan.presentation.model.HabitPresentation
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HabitsListFragment : Fragment() {

    companion object {
        const val HABITS_LIST_FRAGMENT_TAG = "HabitsListFragment"
        private const val TYPE_ARG_PARAM = "TYPE_PARAM"
        private const val HABIT_ARG_PARAM = "HABIT_PARAM"

        fun newInstance(type: com.ivan.domain.model.HabitType?): HabitsListFragment {
            return HabitsListFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_ARG_PARAM, type?.name)
                }
            }
        }
    }

    private lateinit var habitsDatabase: com.ivan.data.database.HabitsDatabase
    private lateinit var habitsDao: com.ivan.data.database.HabitsDao

    private lateinit var habitsRepository: HabitsRepository

    private lateinit var authInterceptor: com.ivan.data.remote.AuthInterceptor
    private lateinit var habitsService: com.ivan.data.remote.HabitsService

    private lateinit var viewModel: HabitsListViewModel

    private lateinit var binding: FragmentHabitsListBinding
    private lateinit var habitAdapter: HabitAdapter

    private lateinit var habits: MutableList<HabitPresentation>
    private var type: com.ivan.domain.model.HabitType? = null

    private var filters: ((HabitPresentation) -> Boolean)? = null
    private var priorityOrder: com.ivan.domain.model.HabitOrder? = null

    private val token = "64f15871-0c48-4db0-9c3f-690ba8d8b6a7"

    private val habitItemClickListener = object : OnItemClickListener<HabitPresentation> {
        override fun onItemClicked(item: HabitPresentation) {
            openAddEditHabitFragment(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString((TYPE_ARG_PARAM))?.let { typeString ->
                com.ivan.domain.model.HabitType.valueOf(typeString)
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
        initHabitsApi()
        initRepository()
        initViewModel()

        updateDataOnServer()

        showBottomSheet()
        initSearchView()
        initRadioGroup()
        initResetButton()

        binding.buttonAdd.setOnClickListener {
            openAddEditHabitFragment(null)
        }

        Log.d("DEBUGGG", "HabitsListFragment end of onViewCreated")
    }

    private fun initRepository() {
        habitsRepository = HabitsRepositoryImpl(habitsService, habitsDao, token)
    }

    private fun updateDataOnServer() {
        viewModel.updateDataOnServer()
    }

    private fun initHabitsApi() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        habitsService = Retrofit.Builder()
            .baseUrl(com.ivan.data.remote.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(com.ivan.data.remote.HabitsService::class.java)
    }

    private fun initDatabase() {
        habitsDatabase = Room.databaseBuilder(
            requireContext().applicationContext,
            com.ivan.data.database.HabitsDatabase::class.java,
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
            priorityOrder = com.ivan.domain.model.HabitOrder.ASCENDING
            viewModel.showHabitsWithFilters({ it.type == type }, priorityOrder ?: com.ivan.domain.model.HabitOrder.NONE)
        }

        binding.fragmentHabitsListOrderByPriorityDescending.setOnClickListener {
            priorityOrder = com.ivan.domain.model.HabitOrder.DESCENDING
            viewModel.showHabitsWithFilters({ it.type == type }, priorityOrder ?: com.ivan.domain.model.HabitOrder.NONE)
        }
    }

    private fun resetFilters() {
        viewModel.showHabitsWithFilters({ it.type == type }, com.ivan.domain.model.HabitOrder.NONE)
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
            HabitsListViewModelFactory(filters, habitsDao, habitsRepository)
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
            priorityOrder = priorityOrder ?: com.ivan.domain.model.HabitOrder.NONE
        )
    }

    private fun initRecyclerView() {
        habitAdapter = HabitAdapter(habitItemClickListener)
        binding.recyclerviewHabitsList.adapter = habitAdapter
    }
}