package com.example.zoomkotlinproject.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import us.zoom.sdk.MeetingActivity

class MyMeetingActivity : MeetingActivity() {

    val handler = Handler(Looper.getMainLooper())
    private lateinit var viewModel: MainViewModel

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this) { render(it) }
        handler.postDelayed(object : Runnable {
            override fun run() {
                viewModel.onEvent(
                    MainViewEvent.VerifyTokenEvent(
                        this@MyMeetingActivity,
                        "Bearer ${
                            SharedPref.readPrefString(
                                this@MyMeetingActivity,
                                Constants.TOKEN
                            )
                        }"
                    )
                )
                handler.postDelayed(this, 1000 * 60 * 1)
            }
        }, 1000 * 60 * 1)
    }

    private fun render(viewState: MainViewState?) {
        if (viewState == null) return

        viewState.verifyTokenResponse?.let {

        }

        viewState.error?.let {
            SharedPref.clear(this@MyMeetingActivity)
            Toast.makeText(this,"Logged in from another device",Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MyMeetingActivity, LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        onClickLeave()
    }
}