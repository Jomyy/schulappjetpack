package com.jomy.schulapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class SchulApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            "schulapp_channel",
            "BackgroundServices",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
        notificationManager.cancelAll()
    }


}
