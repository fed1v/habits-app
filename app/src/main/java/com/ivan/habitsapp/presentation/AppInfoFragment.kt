package com.ivan.habitsapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ivan.habitsapp.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {
    companion object {
        fun newInstance(): AppInfoFragment {
            return AppInfoFragment()
        }
    }

    private lateinit var binding: FragmentAppInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
}