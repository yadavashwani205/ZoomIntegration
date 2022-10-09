package com.example.zoomkotlinproject.view

import us.zoom.sdk.MeetingActivity

class MyMeetingActivity : MeetingActivity() {

    override fun onBackPressed() {
        onClickLeave()
    }
}