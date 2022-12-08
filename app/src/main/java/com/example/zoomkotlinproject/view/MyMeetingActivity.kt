package com.example.zoomkotlinproject.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.utils.SharedPref
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import us.zoom.sdk.MeetingActivity

class MyMeetingActivity : MeetingActivity() {

    private val handler = Handler(Looper.getMainLooper())
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
    }

    override fun onBackPressed() {
        onClickLeave()
    }
}