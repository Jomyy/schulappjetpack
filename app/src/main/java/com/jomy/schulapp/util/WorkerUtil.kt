package com.jomy.schulapp.util

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jomy.schulapp.background.FetchApiWorker
import java.io.File
import java.lang.Exception
import java.time.Duration
import java.util.concurrent.TimeUnit

object WorkerUtil {


    @JvmStatic
    fun addWorker(applicationContext: Context) {
        try {
            val workManager = WorkManager.getInstance(applicationContext)

            if (SettingsUtil.readSetting("notification_class", applicationContext) == "") {
                workManager.cancelAllWork()
                return
            }
            val fetchRequest = PeriodicWorkRequestBuilder<FetchApiWorker>(
                15, TimeUnit.MINUTES

            ).build()


            workManager.enqueueUniquePeriodicWork(
                "schulapp_fetch",
                ExistingPeriodicWorkPolicy.REPLACE, fetchRequest
            )
        } catch (
            e: Exception
        ) {

        }
    }
}
