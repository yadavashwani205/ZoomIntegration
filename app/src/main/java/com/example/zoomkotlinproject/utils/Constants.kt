package com.example.zoomkotlinproject.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

object Constants {

    const val SOMETHING_WENT_WRONG: String = "Something went wrong"
    const val USER_NAME = "USER_NAME"
    const val LOGIN_ID = "LOGIN_ID"
    const val LOGIN_PASSWORD = "PASSWORD"
    const val TOKEN = "TOKEN"
    const val REMAINING_DAYS = "REMAINING_DAYS"
    const val LATEST_APP_VERSION="LATEST_APP_VERSION"
    const val MIN_APP_VERSION="MIN_APP_VERSION"
    const val IS_NOW_LOGGED_IN = "IS_NOW_LOGGED_IN"
    const val APP_KEY="APP_KEY"
    const val APP_SECRET="APP_SECRET"


    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun <T> getErrorBodyMessage(response: Response<T>): String {
        Log.d("response_error", response.toString())
        return try {
            if (response.code() < 500) {
                val errorBody = response.errorBody()
                val errorObject = JSONObject(errorBody?.charStream()?.readText() ?: "")
                errorObject.get("message").toString()
            } else {
                "Something went wrong, Please try again!"
            }
        } catch (e: JSONException) {
            "Something went wrong, Please try again!"
        }
    }
    fun <T> hasTokenExpired(response: Response<T>): String {
        Log.d("response_error", response.toString())
        return try {
            if (response.code() < 500) {
                val errorBody = response.errorBody()
                val errorObject = JSONObject(errorBody?.charStream()?.readText() ?: "")
                errorObject.get("hasTokenExpired").toString()
            } else {
                "false"
            }
        } catch (e: JSONException) {
            "false"
        }
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val n = cm.activeNetwork
        if (n != null) {
            val nc = cm.getNetworkCapabilities(n)
            //It will check for both wifi and cellular network
            return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            )
        }
        return false
    }

}