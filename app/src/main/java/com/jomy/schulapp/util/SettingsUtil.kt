package com.jomy.schulapp.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.lang.reflect.Type

object SettingsUtil {
    private var gson = Gson()
    private var settings: MutableList<StringSelectSetting> = mutableListOf()

    @JvmStatic
    fun readSetting(key: String, context: Context): String {
        readSettings(context)

        return try {
            settings.getByKey(key)
        } catch (e: Exception) {
            ""
        }

    }

    @JvmStatic
    fun writeSetting(key: String, newSetting: String, context: Context) {


        settings = settings.setByKey(key, newSetting)

        writeSettings(context = context)
    }

    @JvmStatic
    fun readSettings(context: Context) {
        if (File(context.getExternalFilesDir(null)?.absoluteFile?.path + "/settings.json").exists()) {
            try {
                val type: Type = object : TypeToken<List<StringSelectSetting>>() {}.type
                settings = gson.fromJson(
                    FileReader(context.getExternalFilesDir(null)?.absoluteFile?.path + "/settings.json").readText(),
                    type
                )

            } catch (
                e: Exception
            ) {


            }


        }

    }

    @JvmStatic
    fun writeSettings(context: Context) {

        val writer = File(context.getExternalFilesDir(null), "settings.json")
        writer.writeText(gson.toJson(settings.toList()))

    }
}

fun MutableList<StringSelectSetting>.getByKey(key: String): String {
    this.forEach { item ->
        if (item.key == key) {
            return item.value
        }

    }
    return ""
}

fun MutableList<StringSelectSetting>.setByKey(
    key: String,
    value: String
): MutableList<StringSelectSetting> {
    val tempList = this
    var exists = false
    this.forEach { item ->
        if (item.key == key) {
            tempList[this.indexOf(item)] = StringSelectSetting(item.key, value)
            exists = true
        }
    }
    if (!exists) {
        tempList.add(StringSelectSetting(key, value))
    }

    return tempList

}