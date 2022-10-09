package com.example.zoomkotlinproject.repository

import android.content.Context
import android.content.Intent
import com.example.zoomkotlinproject.model.*
import com.example.zoomkotlinproject.network.ZoomIntegrationApi
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.Resource
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.view.LoginActivity
import retrofit2.Response
import java.lang.Exception

class MainRepository(private val api: ZoomIntegrationApi) : MainRepositoryImpl {
    override suspend fun login(userName: String, password: String): Resource<LoginResponse> {
        val response: Response<LoginResponse>
        try {
            response = api.login(userName, password)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(LoginResponse(message = Constants.SOMETHING_WENT_WRONG))
        }
        return if (!response.isSuccessful) {
            Resource.Error(
                LoginResponse(
                    success = false,
                    message = Constants.getErrorBodyMessage(response),
                    data = null
                )
            )
        } else {
            return if (response.body() == null) {
                Resource.Error(
                    LoginResponse(
                        success = false,
                        message = response.message(),
                        data = null
                    )
                )
            } else {
                when (response.body()!!.success) {
                    true -> {
                        Resource.Content(response.body()!!)
                    }
                    else -> {
                        Resource.Error(response.body()!!)
                    }
                }
            }
        }
    }

    override suspend fun getMeetings(context: Context, token: String): Resource<MeetingResponse> {
        val response: Response<MeetingResponse>
        try {
            response = api.getMeetings(token)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(MeetingResponse(message = Constants.SOMETHING_WENT_WRONG))
        }
        return if (!response.isSuccessful) {
            if (Constants.hasTokenExpired(response).contains("true", true)) {
                SharedPref.clear(context)
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
            Resource.Error(
                MeetingResponse(
                    success = false,
                    message = Constants.getErrorBodyMessage(response),
                    data = null
                )
            )
        } else {
            return if (response.body() == null) {
                Resource.Error(
                    MeetingResponse(
                        success = false,
                        message = response.message(),
                        data = null
                    )
                )
            } else {
                when (response.body()!!.success) {
                    true -> {
                        Resource.Content(response.body()!!)
                    }
                    else -> {
                        if (response.body()?.hasTokenExpired == true) {
                            SharedPref.clear(context)
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                        Resource.Error(response.body()!!)
                    }
                }
            }
        }
    }

    override suspend fun logout(context: Context, token: String): Resource<LogoutResponse> {
        val response: Response<LogoutResponse>
        try {
            response = api.logOut(token)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(LogoutResponse(message = Constants.SOMETHING_WENT_WRONG))
        }
        return if (!response.isSuccessful) {
            Resource.Error(
                LogoutResponse(
                    success = false,
                    message = Constants.getErrorBodyMessage(response),
                )
            )
        } else {
            return if (response.body() == null) {
                Resource.Error(
                    LogoutResponse(
                        success = false,
                        message = response.message(),
                    )
                )
            } else {
                when (response.body()!!.success) {
                    true -> {
                        Resource.Content(response.body()!!)
                    }
                    else -> {
                        if (response.body()?.hasTokenExpired == true) {
                            SharedPref.clear(context)
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                        Resource.Error(response.body()!!)
                    }
                }
            }
        }
    }

    override suspend fun changePassword(
        context: Context,
        token: String,
        oldPassword: String,
        newPassword: String
    ): Resource<ChangePasswordResponse> {
        val response: Response<ChangePasswordResponse>
        try {
            response = api.changePassword(token, oldPassword, newPassword)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(ChangePasswordResponse(message = Constants.SOMETHING_WENT_WRONG))
        }
        return if (!response.isSuccessful) {
            Resource.Error(
                ChangePasswordResponse(
                    success = false,
                    message = Constants.getErrorBodyMessage(response),
                )
            )
        } else {
            return if (response.body() == null) {
                Resource.Error(
                    ChangePasswordResponse(
                        success = false,
                        message = response.message(),
                    )
                )
            } else {
                when (response.body()!!.success) {
                    true -> {
                        Resource.Content(response.body()!!)
                    }
                    else -> {
                        if (response.body()?.hasTokenExpired == true) {
                            SharedPref.clear(context)
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                        Resource.Error(response.body()!!)
                    }
                }
            }
        }
    }

    override suspend fun getMatchSchedule(): Resource<MatchScheduleResponse> {
        val response: Response<MatchScheduleResponse>
        try {
            response = api.getMatchSchedule()
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error(MatchScheduleResponse(message = Constants.SOMETHING_WENT_WRONG))
        }
        return if (!response.isSuccessful) {
            Resource.Error(
                MatchScheduleResponse(
                    success = false,
                    message = Constants.getErrorBodyMessage(response),
                    data = null
                )
            )
        } else {
            return if (response.body() == null) {
                Resource.Error(
                    MatchScheduleResponse(
                        success = false,
                        message = response.message(),
                        data = null
                    )
                )
            } else {
                when (response.body()!!.success) {
                    true -> {
                        Resource.Content(response.body()!!)
                    }
                    else -> {
                        Resource.Error(response.body()!!)
                    }
                }
            }
        }
    }

}