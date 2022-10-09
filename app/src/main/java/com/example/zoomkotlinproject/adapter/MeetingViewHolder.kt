package com.example.zoomkotlinproject.adapter

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.zoomkotlinproject.R
import com.example.zoomkotlinproject.databinding.ItemMeetingBinding
import com.example.zoomkotlinproject.model.Meeting
import com.example.zoomkotlinproject.view.MeetingClickListener

class MeetingViewHolder(private val binding: ItemMeetingBinding,private val clickListener: MeetingClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(item: Meeting) {
        binding.meetingName.text = item.name
        binding.meetingLayout.setOnClickListener {
            AlertDialog.Builder(binding.root.context).setTitle("Permission")
                .setMessage("Do you want to start audio")
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    startMeeting(item,false)
                    dialog.dismiss()
                }.setNegativeButton(R.string.no) { dialog, _ ->
                    startMeeting(item,true)
                    dialog.dismiss()
                }.show()
        }

    }

    private fun startMeeting(meeting: Meeting, isAudible: Boolean){
        clickListener.startMeeting(meeting,isAudible)
    }
}