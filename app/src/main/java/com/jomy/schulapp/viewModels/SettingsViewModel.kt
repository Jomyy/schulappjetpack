package com.jomy.schulapp.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.util.*
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _allClasses = mutableListOf<String>()
    val allClasses: List<String> get() = _allClasses
    private var errorMessage: String by mutableStateOf("")
    var isNoteEnabled = mutableStateOf(false)

    fun loadSettings(context: Context) {
        viewModelScope.launch {
            SharedPrefsUtil.readBooleanSetting(Keys.NOTIFICATIONS_ENABLED, context).collect {
                isNoteEnabled.value = it
            }
            SharedPrefsUtil.readStringSetting(Keys.SELECTED_NOTIFICATION_KLASSE, context).collect {
                _selectedNotKlasse.value = it
            }
        }
    }

    fun setNotificationActive(context: Context) {
        viewModelScope.launch {
            SharedPrefsUtil.readBooleanSetting(Keys.NOTIFICATIONS_ENABLED, context).collect {
                isNoteEnabled.value = it
            }
        }

    }

    fun loadClasses() {
        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                _allClasses.clear()
                _allClasses.addAll(apiService.getAllClasses())


            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }

    }

    private val _selectedNotKlasse = mutableStateOf("")
    fun addWorker(context: Context) {
        viewModelScope.launch {
            Log.d("PREFSSS", isNoteEnabled.value.toString())
            WorkerUtil.addWorker(context, !isNoteEnabled.value)
        }
    }


}