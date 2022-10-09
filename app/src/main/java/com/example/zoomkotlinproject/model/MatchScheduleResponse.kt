package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchScheduleResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean? = null,
    @SerializedName("hasTokenExpired")
    @Expose
    val hasTokenExpired: Boolean? = null,
    @SerializedName("data")
    @Expose
    val data: List<MatchSchedule>? = null,
    @SerializedName("message")
    @Expose
    val message: String? = null
)
