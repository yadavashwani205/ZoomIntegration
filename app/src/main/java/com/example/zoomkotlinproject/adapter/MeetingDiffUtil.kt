package com.example.zoomkotlinproject.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.zoomkotlinproject.model.Meeting

class MeetingDiffUtil : DiffUtil.ItemCallback<Meeting>() {
    override fun areItemsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
        return oldItem == newItem
    }
}