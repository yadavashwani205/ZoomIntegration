package com.example.zoomkotlinproject.utils

sealed class Resource<T>{
    data class Content<T>(val packet: T): Resource<T>()
    data class Error<T>(val packet: T): Resource<T>()
}
