package com.jomy.schulapp.background

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jomy.schulapp.MainActivity
import com.jomy.schulapp.R
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.WorkerUtil
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import kotlin.random.Random

class FetchApiWorker(private val context: Context, private val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    val _subs = mutableStateListOf<List<String>>()
    val subs: List<List<String>> get() = _subs
    var errorMessage: String by mutableStateOf("")

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun doWork(): Result {


        val apiService = APIService.getInstance()
        try {
            var gson = Gson()
            val typeToken = object : TypeToken<List<List<String>>>() {}

            var selectedKlasse = "";
            var before: List<List<String>>
            try {
                val reader: FileReader = FileReader(context.cacheDir.path + "/" + "classes.json")
                before = gson.fromJson(reader.readText(), typeToken.type)

                selectedKlasse = SettingsUtil.readSetting("notification_class", context = context)
            } catch (e: java.lang.Exception) {
                before = listOf(listOf(""));
            }

            val after: List<List<String>> = apiService.getSubsNext()

            var jsonString: String = gson.toJson(after)

            var onlyMyClass = mutableListOf(mutableListOf(""));
            var onlyMyClassBefore = mutableListOf(mutableListOf(""));

            after.forEach { item ->
                if(item[0] == selectedKlasse){
                    onlyMyClass.add(item.toMutableList())
                }
            }

            before.forEach { item->
                if(item[0] == selectedKlasse){
                    onlyMyClassBefore.add(item.toMutableList())
                }
            }


            val file = File(context.cacheDir, "classes.json")
            file.writeText(jsonString)
            if (onlyMyClass.deepEquals(onlyMyClassBefore)) {

                return Result.success()
            } else  {
                startForegroundService(selectedKlasse)
                return Result.success()

            }





        } catch (e: Exception) {



        }

        return Result.success()
    }

    fun List<List<String>>.deepEquals(other: List<List<String>>): Boolean {

    if(this.size == 0 || other.size == 0){
        return true;
    }
        this.forEach({ item1 ->
            val firstIndex = this.indexOf(item1)
            item1.forEach({ item2 ->
                val secondIndex = item1.indexOf(item2)
                try{
                    if (other[firstIndex][secondIndex] != item2) {
                        return false;
                    }
                }catch (
                    e:java.lang.Exception
                ){
                    return false;
                }

            })
        })
        return true;
    }
    fun List<List<String>>.containsNested(item: String): Boolean{
        this.forEach { item1->
            if(item1[0] == item){
                return true;
            }

        }
        return false
    }

    private suspend fun startForegroundService(klasse: String) {

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(applicationContext, MainActivity::class.java,).setAction(Intent.ACTION_MAIN).putExtra("note","subsnextpage").putExtra("klasse",klasse)
        val contentIntent = PendingIntent.getActivity(context, 0, resultIntent,  PendingIntent.FLAG_IMMUTABLE);

        notificationManager.notify(0,
            NotificationCompat.Builder(context, "schulapp_channel")
                .setSmallIcon(R.mipmap.ic_launcher).setContentText("Dein Vertretungsplan hat sich geäandert").setContentTitle(klasse)
                .setAutoCancel(true).setContentIntent(contentIntent).build()
        )


    }

}