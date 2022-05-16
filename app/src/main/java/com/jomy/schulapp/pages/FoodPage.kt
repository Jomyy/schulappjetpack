package com.jomy.schulapp.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material3.*
import com.jomy.schulapp.components.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jomy.schulapp.R


import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.dataclasses.FoodDay
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoodPage(model: FoodPageViewModel) {
    LaunchedEffect(Unit, block = {
        model.loadFood()
    })
    val isRefreshing by model.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { model.refresh() },
    ) {
        if (model.errorMessage.isEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 15.dp,
                        end = 15.dp
                    )
                    .fillMaxHeight()
                    .fillMaxWidth(),

                ) {
                item {
                    Divider(
                        modifier = Modifier
                            .padding(7.dp)
                            .height(0.dp)
                    )
                }
                items(model.food) { foodDay ->


                    FoodCard(foodData = FoodDay(foodDay[0], foodDay[1], foodDay[2], foodDay[3]))
                    Divider(
                        modifier = Modifier
                            .padding(7.dp)
                            .height(0.dp)
                            .animateItemPlacement()

                    )
                }
            }
            if (model.food.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    CircularProgressIndicator()
                }

            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Rounded.SignalWifiStatusbarConnectedNoInternet4,
                    "WifiOff",
                    modifier = Modifier.size(45.dp),
                )
                Text(
                    stringResource(id = R.string.serverNotOn),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )

                TextButton(onClick = { model.refresh() }) {
                    Text("Erneut Versuchen", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }


}

class FoodPageViewModel : ViewModel() {
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
            delay(300)
            val apiService = APIService.getInstance()

            try {
                _food.clear()
                _food.addAll(apiService.getFood())
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
            _isRefreshing.value = false
        }

    }
}