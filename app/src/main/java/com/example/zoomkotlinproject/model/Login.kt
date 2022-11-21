package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("token")
    @Expose
    val token: String? = null,
    @SerializedName("user")
    @Expose
    val user: User? = null,
    @SerializedName("app_key")
    @Expose
    val appKey: String? = null,
    @SerializedName("app_secret")
    @Expose
    val appSecret: String? = null,
    @SerializedName("min_apk_version")
    @Expose
    val min_apk_version: Int? = null,
    @SerializedName("latest_apk_version")
    @Expose
    val latest_apk_version: Int? = null
)
