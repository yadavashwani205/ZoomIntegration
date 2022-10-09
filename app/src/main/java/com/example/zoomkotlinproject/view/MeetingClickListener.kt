package com.example.zoomkotlinproject.view

import com.example.zoomkotlinproject.model.Meeting

interface MeetingClickListener {

    fun startMeeting(meeting: Meeting,isAudible:Boolean)
}