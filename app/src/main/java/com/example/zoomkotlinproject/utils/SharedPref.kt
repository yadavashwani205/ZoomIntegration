package com.example.zoomkotlinproject.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object SharedPref {
    private const val PREF_NAME = "USER_PREF"

    fun writePrefString(context: Context, key: String, value: String) {
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun readPrefString(context: Context, key: String): String? {
        return context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getString(key, "")
    }

    fun writePrefInt(context: Context, key: String, value: Int) {
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun readPrefInt(context: Context, key: String): Int {
        return context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).getInt(key, 0)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().clear().apply()
    }
}