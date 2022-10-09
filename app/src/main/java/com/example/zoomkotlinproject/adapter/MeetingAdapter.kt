package com.example.zoomkotlinproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.zoomkotlinproject.databinding.ItemMeetingBinding
import com.example.zoomkotlinproject.model.Meeting
import com.example.zoomkotlinproject.view.MeetingClickListener

class MeetingAdapter(private val clickListener: MeetingClickListener) :
    ListAdapter<Meeting, MeetingViewHolder>(MeetingDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        return MeetingViewHolder(
            ItemMeetingBinding.inflate(LayoutInflater.from(parent.context),parent,false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}