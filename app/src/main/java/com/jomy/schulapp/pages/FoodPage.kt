package com.jomy.schulapp.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jomy.schulapp.R
import com.jomy.schulapp.dataclasses.FoodDay
import com.jomy.schulapp.viewModels.FoodViewModel
import com.jomy.schulapp.viewModels.SubsNextViewModel
import com.jomy.schulapp.viewModels.SubsViewModel

@Composable
fun FoodPage(
    model: FoodViewModel,
    subsViewModel: SubsViewModel,
    subsNextViewModel: SubsNextViewModel
) {

    val isRefreshing by model.isRefreshing.collectAsState()


    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            model.refresh()
            subsViewModel.loadSubs()
            subsNextViewModel.loadSubs()
        },

        ) {
        if (model.errorMessage.isEmpty()) {

            LazyVerticalGrid(
                modifier = Modifier
                    .padding(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 15.dp,
                        end = 15.dp
                    )
                    .fillMaxHeight()
                    .fillMaxWidth(),
                columns = GridCells.Adaptive(400.dp),

                ) {

                items(model.food.size) { foodDay ->

                    Column(modifier = Modifier.padding(7.dp)) {
                        FoodCard(
                            foodData = FoodDay(
                                model.food[foodDay][0],
                                model.food[foodDay][1],
                                model.food[foodDay][2],
                                model.food[foodDay][3]
                            )
                        )
                    }


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

                TextButton(onClick = {
                    model.refresh()
                    subsViewModel.loadSubs()
                    subsNextViewModel.loadSubs()
                }) {
                    Text("Erneut Versuchen", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }


}

