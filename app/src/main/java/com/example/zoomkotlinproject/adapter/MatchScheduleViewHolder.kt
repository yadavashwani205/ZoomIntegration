package com.example.zoomkotlinproject.adapter

import android.text.format.DateFormat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zoomkotlinproject.R
import com.example.zoomkotlinproject.databinding.ItemMatchScheduleBinding
import com.example.zoomkotlinproject.model.MatchSchedule
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class MatchScheduleViewHolder(private val binding: ItemMatchScheduleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MatchSchedule) {
        binding.name.text = item.matchName
        binding.venue.text = item.address
        binding.firstTeamName.text = item.team1Name
        binding.secondTeamName.text = item.team2Name
        if (item.date != null)
            getDate(item.date, binding)
        Glide.with(binding.root).load(item.flag1).placeholder(R.drawable.nxt_logo)
            .into(binding.firstTeamFlag)
        Glide.with(binding.root).load(item.flag2).placeholder(R.drawable.nxt_logo)
            .into(binding.secondTeamFlag)
    }


    private fun getDate(dateStr: String, binding: ItemMatchScheduleBinding) {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = format.parse(dateStr)
            binding.date.text = DateFormat.format("dd", date)
            binding.day.text = DateFormat.format("EEEE", date)
            binding.month.text = DateFormat.format("MMM", date)
            binding.time.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}