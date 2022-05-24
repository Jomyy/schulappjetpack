package com.jomy.schulapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.api.APIService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val _food = mutableStateListOf<List<String>>()
    val food: List<List<String>> get() = _food
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    var errorMessage: String by mutableStateOf("")
    fun loadFood() {

        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {

                _food.clear()

                _food.addAll(apiService.getFood())

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }


    }

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch {

            val apiService = APIService.getInstance()

            errorMessage = try {
                _food.clear()
                delay(300)
                _food.addAll(apiService.getFood())
                ""
            } catch (e: Exception) {
                e.message.toString()
            }
            _isRefreshing.value = false
        }

    }
}