package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchSchedule(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("team1_name")
    @Expose
    val team1Name: String? = null,
    @SerializedName("flag1")
    @Expose
    val flag1: String? = null,
    @SerializedName("team2_name")
    @Expose
    val team2Name: String? = null,
    @SerializedName("flag2")
    @Expose
    val flag2: String? = null,
    @SerializedName("match_name")
    @Expose
    val matchName: String? = null,
    @SerializedName("address")
    @Expose
    val address: String? = null,
    @SerializedName("date")
    @Expose
    val date: String? = null
)
