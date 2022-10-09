package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    @Expose
    val success: Boolean? = null,
    @SerializedName("data")
    @Expose
    val data: Login? = null,
    @SerializedName("message")
    @Expose
    val message: String? = null,
)
