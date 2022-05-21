package com.jomy.schulapp.background

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jomy.schulapp.MainActivity
import com.jomy.schulapp.R
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.util.SettingsUtil
import java.io.File
import java.io.FileReader

class FetchApiWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {


        val apiService = APIService.getInstance()
        try {
            val gson = Gson()
            val typeToken = object : TypeToken<List<List<String>>>() {}

            var selectedKlasse = ""
            var before: List<List<String>>
            try {
                val reader: FileReader = FileReader(context.cacheDir.path + "/" + "classes.json")
                before = gson.fromJson(reader.readText(), typeToken.type)

                selectedKlasse = SettingsUtil.readSetting("notification_class", context = context)
            } catch (e: java.lang.Exception) {
                before = listOf(listOf(""))
            }

            val after: List<List<String>> = apiService.getSubsNext()

            val jsonString: String = gson.toJson(after)

            val onlyMyClass = mutableListOf(mutableListOf(""))
            val onlyMyClassBefore = mutableListOf(mutableListOf(""))

            after.forEach { item ->
                if (item[0] == selectedKlasse) {
                    onlyMyClass.add(item.toMutableList())
                }
            }

            before.forEach { item ->
                if (item[0] == selectedKlasse) {
                    onlyMyClassBefore.add(item.toMutableList())
                }
            }


            val file = File(context.cacheDir, "classes.json")
            file.writeText(jsonString)
            return if (onlyMyClass.deepEquals(onlyMyClassBefore)) {

                Result.success()
            } else {
                startForegroundService(selectedKlasse)
                Result.success()

            }


        } catch (e: Exception) {


        }

        return Result.success()
    }

    private fun List<List<String>>.deepEquals(other: List<List<String>>): Boolean {

        if (this.isEmpty() || other.isEmpty()) {
            return true
        }
        this.forEach { item1 ->
            val firstIndex = this.indexOf(item1)
            item1.forEach { item2 ->
                val secondIndex = item1.indexOf(item2)
                try {
                    if (other[firstIndex][secondIndex] != item2) {
                        return false
                    }
                } catch (
                    e: java.lang.Exception
                ) {
                    return false
                }

            }
        }
        return true
    }


    private fun startForegroundService(klasse: String) {

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent =
            Intent(applicationContext, MainActivity::class.java).setAction(Intent.ACTION_MAIN)
                .putExtra("note", "subsnextpage").putExtra("klasse", klasse)
        val contentIntent =
            PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE)

        notificationManager.notify(
            0,
            NotificationCompat.Builder(context, "schulapp_channel")
                .setSmallIcon(R.drawable.ic_icon)
                .setContentText("Dein Vertretungsplan hat sich geändert").setContentTitle(klasse)
                .setAutoCancel(true).setContentIntent(contentIntent).build()
        )


    }

}