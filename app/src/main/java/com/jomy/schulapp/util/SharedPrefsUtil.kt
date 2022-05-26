package com.jomy.schulapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SharedPrefsUtil {
    @JvmStatic
    suspend fun writeStringSetting(key: String, value: String, context: Context) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    fun readStringSetting(key: String, context: Context): Flow<String> {


        return context.dataStore.data.map {
            it[stringPreferencesKey(key)] ?: ""
        }


    }

    @JvmStatic
    suspend fun writeBooleanSetting(key: String, value: Boolean, context: Context) {
        context.dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    @JvmStatic
    fun readBooleanSetting(key: String, context: Context): Flow<Boolean> {


        return context.dataStore.data.map {
            it[booleanPreferencesKey(key)] ?: false
        }


    }

}