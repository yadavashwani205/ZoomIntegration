package com.example.zoomkotlinproject.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meeting(
    @SerializedName("id")
    @Expose
    val id: Long? = null,
    @SerializedName("meeting_id")
    @Expose
    val meetingNo: String? = null,
    @SerializedName("passcode")
    @Expose
    val password: String? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("customer_name")
    @Expose
    val customerName: String? = null
) : Parcelable

