package com.example.zoomkotlinproject.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoomkotlinproject.network.ZoomIntegrationApi
import com.example.zoomkotlinproject.network.ZoomIntegrationRetro
import com.example.zoomkotlinproject.repository.MainRepository
import com.example.zoomkotlinproject.utils.Resource
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewResult
import com.example.zoomkotlinproject.viewstate.MainViewState
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val mutableLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    val viewState: LiveData<MainViewState>
        get() = mutableLiveData

    private var currentViewState = MainViewState()
        set(value) {
            field = value
            mutableLiveData.value = value
        }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is MainViewEvent.LoginEvent -> login(event.userName, event.password, event.device_token)
            is MainViewEvent.GetMeetingEvent -> getMeetings(event.context, event.token)
            is MainViewEvent.LogoutEvent -> logout(event.context, event.token)
            is MainViewEvent.ChangePasswordEvent -> changePassword(
                event.context,
                event.token,
                event.oldPassword,
                event.newPassword
            )
            MainViewEvent.GetMatchScheduleEvent -> getMatchSchedule()
            is MainViewEvent.VerifyTokenEvent -> verifyToken(event.context, event.token)
        }
    }

    private fun verifyToken(context: Context, token: String) {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result = MainRepository(instance).verifyToken(context, token)) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.VerifyTokenResult(
                            verifyTokenResponse = result.packet,
                            verifyTokenError = null
                        )
                    )
                )
                is Resource.Error -> {
                    if(result.packet.success == false || result.packet.hasTokenExpired==true)
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.VerifyTokenResult(
                                verifyTokenResponse = null,
                                verifyTokenError = result.packet.message
                            )
                        )
                    )
                    else
                        Resource.Content(
                            MainViewResult.VerifyTokenResult(
                                verifyTokenResponse = result.packet,
                                verifyTokenError = null
                            )
                        )
                }
            }
        }
    }

    private fun changePassword(
        context: Context,
        token: String,
        oldPassword: String,
        newPassword: String
    ) {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result =
                MainRepository(instance).changePassword(context, token, oldPassword, newPassword)) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.ChangePasswordResult(
                            changePasswordResponse = result.packet,
                            error = null
                        )
                    )
                )
                is Resource.Error ->
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.ChangePasswordResult(
                                changePasswordResponse = null,
                                error = result.packet.message
                            )
                        )
                    )
            }

        }

    }

    private fun logout(context: Context, token: String) {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result = MainRepository(instance).logout(context, token)) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.LogoutResult(
                            logoutResponse = result.packet,
                            error = null
                        )
                    )
                )
                is Resource.Error ->
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.LogoutResult(
                                logoutResponse = null,
                                error = result.packet.message
                            )
                        )
                    )
            }

        }
    }

    private fun login(userName: String, password: String, deviceToken: String) {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result = MainRepository(instance).login(userName, password, deviceToken)) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.LoginResult(
                            result.packet,
                            null
                        )
                    )
                )
                is Resource.Error ->
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.LoginResult(
                                null,
                                result.packet.message
                            )
                        )
                    )
            }
        }
    }

    private fun getMeetings(context: Context, token: String) {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result = MainRepository(instance).getMeetings(context, token)) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.GetMeetingResult(
                            result.packet,
                            null
                        )
                    )
                )
                is Resource.Error ->
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.GetMeetingResult(
                                null,
                                result.packet.message
                            )
                        )
                    )
            }
        }
    }

    private fun getMatchSchedule() {
        viewModelScope.launch {
            val instance =
                ZoomIntegrationRetro.getRetroInstance().create(ZoomIntegrationApi::class.java)
            when (val result = MainRepository(instance).getMatchSchedule()) {
                is Resource.Content -> resultToViewState(
                    Resource.Content(
                        MainViewResult.GetMatchScheduleResult(
                            result.packet,
                            null
                        )
                    )
                )
                is Resource.Error ->
                    resultToViewState(
                        Resource.Error(
                            MainViewResult.GetMatchScheduleResult(
                                null,
                                result.packet.message
                            )
                        )
                    )
            }
        }
    }

    private fun resultToViewState(result: Resource<MainViewResult>) {
        currentViewState = when (result) {
            is Resource.Content -> when (result.packet) {
                is MainViewResult.LoginResult -> currentViewState.copy(
                    loginResponse = result.packet.loginResponse,
                    error = null,
                    meetingResponse = null,
                    logoutResponse = null,
                    changePasswordResponse = null,
                    matchScheduleResponse = null,
                    verifyTokenResponse = null,
                    verifyTokenError = null
                )
                is MainViewResult.GetMeetingResult -> currentViewState.copy(
                    loginResponse = null,
                    error = null,
                    meetingResponse = result.packet.meetingResponse,
                    logoutResponse = null,
                    changePasswordResponse = null,
                    matchScheduleResponse = null,
                    verifyTokenResponse = null,
                    verifyTokenError = null
                )
                is MainViewResult.LogoutResult -> currentViewState.copy(
                    loginResponse = null,
                    error = null,
                    meetingResponse = null,
                    logoutResponse = result.packet.logoutResponse,
                    changePasswordResponse = null,
                    matchScheduleResponse = null,
                    verifyTokenResponse = null,
                    verifyTokenError = null
                )
                is MainViewResult.ChangePasswordResult -> currentViewState.copy(
                    loginResponse = null,
                    error = null,
                    meetingResponse = null,
                    logoutResponse = null,
                    changePasswordResponse = result.packet.changePasswordResponse,
                    matchScheduleResponse = null,
                    verifyTokenResponse = null,
                    verifyTokenError = null
                )
                is MainViewResult.GetMatchScheduleResult -> currentViewState.copy(
                    loginResponse = null,
                    error = null,
                    meetingResponse = null,
                    logoutResponse = null,
                    changePasswordResponse = null,
                    matchScheduleResponse = result.packet.matchScheduleResponse,
                    verifyTokenResponse = null,
                    verifyTokenError = null
                )
                is MainViewResult.VerifyTokenResult -> currentViewState.copy(
                    loginResponse = null,
                    error = null,
                    meetingResponse = null,
                    logoutResponse = null,
                    changePasswordResponse = null,
                    matchScheduleResponse = null,
                    verifyTokenResponse = result.packet.verifyTokenResponse,
                    verifyTokenError = null
                )
            }
            is Resource.Error -> {
                when (result.packet) {
                    is MainViewResult.LoginResult -> currentViewState.copy(
                        loginResponse = null,
                        error = result.packet.error,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = null
                    )
                    is MainViewResult.GetMeetingResult -> currentViewState.copy(
                        loginResponse = null,
                        error = result.packet.error,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = null
                    )
                    is MainViewResult.LogoutResult -> currentViewState.copy(
                        loginResponse = null,
                        error = result.packet.error,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = null
                    )
                    is MainViewResult.ChangePasswordResult -> currentViewState.copy(
                        loginResponse = null,
                        error = result.packet.error,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = null
                    )
                    is MainViewResult.GetMatchScheduleResult -> currentViewState.copy(
                        loginResponse = null,
                        error = result.packet.error,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = null
                    )
                    is MainViewResult.VerifyTokenResult -> currentViewState.copy(
                        loginResponse = null,
                        error = null,
                        meetingResponse = null,
                        logoutResponse = null,
                        changePasswordResponse = null,
                        matchScheduleResponse = null,
                        verifyTokenResponse = null,
                        verifyTokenError = result.packet.verifyTokenError
                    )
                }
            }
        }
    }
}