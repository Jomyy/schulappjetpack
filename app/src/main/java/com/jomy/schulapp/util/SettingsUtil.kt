package com.jomy.schulapp.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import java.lang.reflect.Type

object SettingsUtil {
    var gson = Gson()
    var settings: MutableList<StringSelectSetting> = mutableListOf()

    @JvmStatic
    fun readSetting(key: String, context: Context): String {
        readSettings(context);

        try {
            return settings.getByKey(key);
        } catch (e: Exception) {
            return ""
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
                e: java.lang.Exception
            ) {


            }


        }

    }

    @JvmStatic
    fun writeSettings(context: Context) {

        var writer = File(context.getExternalFilesDir(null), "settings.json")
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
    val tempList: MutableList<StringSelectSetting> = this;
    var exists: Boolean = false;
    this.forEach { item ->
        if (item.key == key) {
            tempList[this.indexOf(item)] = StringSelectSetting(item.key, value)
            exists = true;
        }
    }
    if (!exists) {
        tempList.add(StringSelectSetting(key, value))
    }

    return tempList;

}