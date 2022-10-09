package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MeetingResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean? = null,
    @SerializedName("hasTokenExpired")
    @Expose
    val hasTokenExpired: Boolean? = null,
    @SerializedName("data")
    @Expose
    val data: MeetingData? = null,
    @SerializedName("message")
    @Expose
    val message: String? = null
)
