package com.example.zoomkotlinproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("id")
    @Expose
    val id: Int? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
    @SerializedName("user_name")
    @Expose
    val userName: String? = null,
    @SerializedName("address")
    @Expose
    val address: String? = null,
    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,
    @SerializedName("deleted_at")
    @Expose
    val deletedAt: Objects? = null
)
