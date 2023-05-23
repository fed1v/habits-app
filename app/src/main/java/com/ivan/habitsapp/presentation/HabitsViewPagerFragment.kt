package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ivan.habitsapp.R
import com.ivan.habitsapp.databinding.FragmentHabitsViewPagerBinding
import com.ivan.domain.model.HabitType
import com.ivan.habitsapp.presentation.adapter.ViewPagerAdapter

class HabitsViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentHabitsViewPagerBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("DEBUGGG", "HabitsViewPagerFragment onCreateView")
        binding = FragmentHabitsViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("DEBUGGG", "HabitsViewPagerFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(
            childFragmentManager, lifecycle, mutableListOf(
                HabitsListFragment.newInstance(com.ivan.domain.model.HabitType.GOOD),
                HabitsListFragment.newInstance(com.ivan.domain.model.HabitType.BAD)
            )
        )

        binding.fragmentHabitsListViewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = viewPagerAdapter
        }

        TabLayoutMediator(
            binding.fragmentHabitsListTabLayout,
            binding.fragmentHabitsListViewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.good)
                else -> tab.text = getString(R.string.bad)
            }
        }.attach()
    }
}