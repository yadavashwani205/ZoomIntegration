package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VerifyTokenResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean? = null,
    @SerializedName("hasTokenExpired")
    @Expose
    val hasTokenExpired: Boolean? = null,
    @SerializedName("message")
    @Expose
    val message: String? = null
)
