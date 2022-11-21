package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MeetingData(
    @SerializedName("meetings")
    @Expose
    val meeting: List<Meeting>? = null,
    @SerializedName("remaining_days")
    @Expose
    val remainingDays: String? = null
)
