package com.example.zoomkotlinproject.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ZoomIntegrationRetro {
//    private const val BaseUrl = "http://34.154.39.59/"
    private const val BaseUrl = "https://nxtlevel.live"

    fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient().build())
            .build()
    }

    private fun okHttpClient(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        okHttpClient.addInterceptor(interceptor)
        return okHttpClient
    }
}