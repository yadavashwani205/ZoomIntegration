package com.example.zoomkotlinproject.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.zoomkotlinproject.model.MatchSchedule

class MatchScheduleDiffUtil : DiffUtil.ItemCallback<MatchSchedule>() {
    override fun areItemsTheSame(oldItem: MatchSchedule, newItem: MatchSchedule): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MatchSchedule, newItem: MatchSchedule): Boolean {
        return oldItem == newItem
    }
}