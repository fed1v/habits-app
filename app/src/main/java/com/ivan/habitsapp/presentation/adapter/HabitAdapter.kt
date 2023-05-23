package com.ivan.habitsapp.presentation.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivan.habitsapp.databinding.HabitItemViewBinding
import com.ivan.habitsapp.util.OnItemClickListener
import com.ivan.presentation.model.HabitPresentation

class HabitAdapter(
    private val listener: OnItemClickListener<HabitPresentation>
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    var habits: MutableList<HabitPresentation> = mutableListOf()
        set(newValue) {
            field.clear()
            field.addAll(newValue)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HabitItemViewBinding.inflate(inflater, parent, false)
        return HabitViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    class HabitViewHolder(
        private val binding: HabitItemViewBinding,
        private val listener: OnItemClickListener<HabitPresentation>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HabitPresentation) {
            binding.textviewHabitTitle.text = item.title
            binding.textviewHabitDescription.text = item.description
            binding.textviewHabitPriority.text = item.priority.toString()
            binding.textviewHabitPeriodicity.text = item.periodicity.toString()
            binding.textviewHabitType.text = item.type.toString()

            binding.triangle.backgroundTintList = ColorStateList.valueOf(item.color)

            binding.root.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}