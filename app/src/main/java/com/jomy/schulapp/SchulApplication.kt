package com.jomy.schulapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jomy.schulapp.background.FetchApiWorker
import com.jomy.schulapp.util.WorkerUtil
import java.io.File
import java.lang.Exception
import java.time.Duration

class SchulApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "schulapp_channel",
                "BackgroundServices",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        WorkerUtil.addWorker(applicationContext)

    }


}
