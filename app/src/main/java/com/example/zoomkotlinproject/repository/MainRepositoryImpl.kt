package com.example.zoomkotlinproject.repository

import android.content.Context
import com.example.zoomkotlinproject.model.*
import com.example.zoomkotlinproject.utils.Resource

interface MainRepositoryImpl {
    suspend fun login(userName: String, password: String): Resource<LoginResponse>

    suspend fun getMeetings(context: Context, token: String): Resource<MeetingResponse>

    suspend fun logout(context: Context, token: String): Resource<LogoutResponse>

    suspend fun changePassword(
        context: Context,
        token: String,
        oldPassword: String,
        newPassword: String
    ): Resource<ChangePasswordResponse>

    suspend fun getMatchSchedule(): Resource<MatchScheduleResponse>

    suspend fun verifyToken(
        context: Context,
        token: String
    ): Resource<VerifyTokenResponse>
}