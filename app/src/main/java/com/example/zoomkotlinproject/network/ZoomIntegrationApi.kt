package com.example.zoomkotlinproject.network

import com.example.zoomkotlinproject.model.*
import com.example.zoomkotlinproject.utils.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ZoomIntegrationApi {

    @POST("api/login")
    suspend fun login(
        @Query("user_name") user_name: String,
        @Query("password") password: String,
        @Query("device_token") deviceToken: String
    ): Response<LoginResponse>

    @GET("api/meetings")
    suspend fun getMeetings(
        @Header("Authorization") token: String
    ): Response<MeetingResponse>

    @POST("api/logout")
    suspend fun logOut(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @POST("api/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Query("oldPassword") oldPassword: String,
        @Query("newPassword") newPassword: String
    ): Response<ChangePasswordResponse>

    @GET("api/schedule")
    suspend fun getMatchSchedule(): Response<MatchScheduleResponse>

    @GET("api/verify-token")
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<VerifyTokenResponse>
}