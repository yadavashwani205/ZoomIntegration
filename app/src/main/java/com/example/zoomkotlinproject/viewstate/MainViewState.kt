package com.example.zoomkotlinproject.viewstate

import android.content.Context
import com.example.zoomkotlinproject.model.*

data class MainViewState(
    val loginResponse: LoginResponse? = null,
    val error: String? = null,
    val meetingResponse: MeetingResponse? = null,
    val logoutResponse: LogoutResponse? = null,
    val changePasswordResponse: ChangePasswordResponse? = null,
    val matchScheduleResponse: MatchScheduleResponse? = null,
)

sealed class MainViewEvent {
    data class LoginEvent(val userName: String, val password: String) : MainViewEvent()
    data class GetMeetingEvent(val context: Context, val token: String) : MainViewEvent()
    data class LogoutEvent(val context: Context, val token: String) : MainViewEvent()
    data class ChangePasswordEvent(
        val context: Context,
        val token: String,
        val oldPassword: String,
        val newPassword: String
    ) : MainViewEvent()

    object GetMatchScheduleEvent : MainViewEvent()
}

sealed class MainViewResult {
    data class LoginResult(val loginResponse: LoginResponse?, val error: String?) : MainViewResult()
    data class GetMeetingResult(val meetingResponse: MeetingResponse?, val error: String?) :
        MainViewResult()

    data class LogoutResult(val logoutResponse: LogoutResponse?, val error: String?) :
        MainViewResult()

    data class ChangePasswordResult(
        val changePasswordResponse: ChangePasswordResponse?,
        val error: String?
    ) : MainViewResult()

    data class GetMatchScheduleResult(
        val matchScheduleResponse: MatchScheduleResponse?,
        val error: String?
    ) : MainViewResult()
}
