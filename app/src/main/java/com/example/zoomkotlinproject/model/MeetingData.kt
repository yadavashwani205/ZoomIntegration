package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MeetingData(
    @SerializedName("meetings")
    @Expose
    val meeting: List<Meeting>? = null,
    @SerializedName("remaining_days")
    @Expose
    val remainingDays: String? = null,
    @SerializedName("app_key")
    @Expose
    val appKey: String? = null,
    @SerializedName("app_secret")
    @Expose
    val appSecret: String? = null
)
