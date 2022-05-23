package com.jomy.schulapp.viewModels

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.util.Keys
import com.jomy.schulapp.util.SharedPrefsUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubsViewModel : ViewModel() {
    private var _selectedSubs = mutableStateListOf<List<String>>()
    val selectedSubs: List<List<String>> get() = _selectedSubs

    private var _allSubs = mutableStateListOf<List<String>>()
    val allSubs: List<List<String>> get() = _allSubs

    val selectedKlasse = mutableStateOf("")
    var errorMessage = mutableStateOf("")

    private var _allKlassen = mutableStateListOf<String>()
    val allKlassen: List<String> get() = _allKlassen
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun setSelectedKlasse(newKlasse: String, context: Context) {
        viewModelScope.launch {
            SharedPrefsUtil.writeStringSetting(Keys.SELECTED_KLASSE, newKlasse, context)
            loadSelectedKlasse(context)
        }

    }

    fun loadSelectedKlasse(context: Context) {
        viewModelScope.launch {
            SharedPrefsUtil.readStringSetting(Keys.SELECTED_KLASSE, context).collect {
                selectedKlasse.value = it
                loadSelectedSubs()
            }


        }
    }

    private fun loadSelectedSubs() {
        _selectedSubs.clear()
        _allKlassen.clear()
        var clssbf = ""
        allSubs.forEach {
            if (it[0] != clssbf) {
                _allKlassen.add(it[0])
            }
            clssbf = it[0]
            if (it[0] == selectedKlasse.value) {
                _selectedSubs.add(it)
            }
        }
    }

    fun loadSubs() {
        _isRefreshing.value = true
        viewModelScope.launch {
            errorMessage.value = ""

            delay(250)
            val instance = APIService.getInstance()
            try {
                _allSubs.clear()
                _allSubs.addAll(instance.getSubs())

            } catch (e: Exception) {
                errorMessage.value = e.toString()
            }
            loadSelectedSubs()
            _isRefreshing.value = false
        }

    }
}