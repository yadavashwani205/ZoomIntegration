package com.example.zoomkotlinproject.view

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.zoomkotlinproject.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    private val channelId = "CHANNEL_ID"
    private val channelName = "CHANNEL_NAME"
    private val requestCode = 101
    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("asasas","received notification")
        if(message.notification!=null)
        generateNotification(message.notification?.title?:"", message.notification?.body?:"")
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MyNotification::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent =
            PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.nxt_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setContent(getRemoteView(title, message))


        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getNotificationChannel()
        }
        notificationManager.notify(0,builder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(){
        val notificationChannel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
    }
    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.firebaseproject", R.layout.notification)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        return remoteViews
    }

}