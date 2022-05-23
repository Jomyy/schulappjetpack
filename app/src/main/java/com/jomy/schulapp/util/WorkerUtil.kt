package com.jomy.schulapp.util

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.jomy.schulapp.background.FetchApiWorker
import java.lang.Exception
import java.util.concurrent.TimeUnit

object WorkerUtil {


    @JvmStatic
    suspend fun addWorker(applicationContext: Context, enabled: Boolean) {
        try {
            val workManager = WorkManager.getInstance(applicationContext)





            Log.d("CANCEL", "JEP")

            if (enabled) {

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
