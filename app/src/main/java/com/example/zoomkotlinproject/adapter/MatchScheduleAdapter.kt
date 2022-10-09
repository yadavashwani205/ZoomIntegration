package com.example.zoomkotlinproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.zoomkotlinproject.databinding.ItemMatchScheduleBinding
import com.example.zoomkotlinproject.model.MatchSchedule

class MatchScheduleAdapter :
    ListAdapter<MatchSchedule, MatchScheduleViewHolder>(MatchScheduleDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchScheduleViewHolder {
        return MatchScheduleViewHolder(ItemMatchScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MatchScheduleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}