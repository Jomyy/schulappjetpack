package com.jomy.jetpacklernentutorial.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import com.jomy.jetpacklernentutorial.components.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.jetpacklernentutorial.R
import com.jomy.jetpacklernentutorial.api.APIService
import com.jomy.jetpacklernentutorial.dataclasses.FoodDay
import kotlinx.coroutines.launch

@Composable
fun FoodPage(model: FoodPageViewModel) {


    LaunchedEffect(Unit, block = {
        model.loadFood()
    })

    if (model.errorMessage.isEmpty()) {

        LazyColumn(
            modifier = Modifier.padding(
                top = 0.dp,
                bottom = 0.dp,
                start = 15.dp,
                end = 15.dp
            )
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
        Text(stringResource(id = R.string.serverNotOn))
    }


}

class FoodPageViewModel : ViewModel() {
    private val _food = mutableStateListOf<List<String>>()
    val food: List<List<String>> get() = _food

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
}