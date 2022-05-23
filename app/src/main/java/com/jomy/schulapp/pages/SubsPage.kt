package com.jomy.schulapp.pages


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jomy.schulapp.R
import com.jomy.schulapp.components.SelectorDialog
import com.jomy.schulapp.components.SubCard
import com.jomy.schulapp.dataclasses.SubData
import com.jomy.schulapp.viewModels.SubsViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsPage(model: SubsViewModel) {
    var showSelector by remember { mutableStateOf(false) }
    val isRefreshing by model.isRefreshing.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {

        model.loadSelectedKlasse(context)

    })

    Scaffold(bottomBar = {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = {
                    showSelector = !showSelector
                }, modifier = Modifier
                    .fillMaxWidth(.9f)
                    .fillMaxHeight(.9f), colors = ButtonDefaults.elevatedButtonColors()
            ) {
                if (model.selectedKlasse.value != "") {
                    Text(stringResource(id = R.string.selectedclass) + model.selectedKlasse.value)
                } else {
                    Text(stringResource(id = R.string.selectclass))
                }

            }
        }

    }) {


        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()

        ) {
            if (showSelector) {
                SelectorDialog(
                    onDismiss = {
                        showSelector = !showSelector

                    },

                    onPositiveClick = { newklasse ->
                        model.setSelectedKlasse(newKlasse = newklasse, context = context)
                        showSelector = !showSelector

                    },
                    klassen = model.allKlassen,
                    oldSelection = model.selectedKlasse.value
                )
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { model.loadSubs() },
            ) {
                if (model.errorMessage.value.isEmpty()) {
                    if (model.allSubs.isNotEmpty()) {
                        if (model.selectedKlasse.value == "") {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                item {
                                    Text(
                                        stringResource(id = R.string.plsselectclass),
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                    )
                                }

                            }
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .padding(
                                        horizontal = it.calculateRightPadding(
                                            LayoutDirection.Ltr
                                        )
                                    )
                                    .padding(
                                        top = 0.dp,
                                        bottom = 0.dp,
                                        start = 15.dp,
                                        end = 15.dp
                                    )
                                    .fillMaxSize(),
                                columns = GridCells.Adaptive(300.dp),


                                ) {

                                items(model.selectedSubs.size) { sub ->


                                    Column(modifier = Modifier.padding(7.dp)) {
                                        SubCard(
                                            subData = SubData(
                                                model.selectedSubs[sub][0],
                                                model.selectedSubs[sub][1],
                                                model.selectedSubs[sub][2],
                                                model.selectedSubs[sub][3],
                                                model.selectedSubs[sub][4],
                                                model.selectedSubs[sub][5],
                                                model.selectedSubs[sub][6]
                                            )
                                        )


                                    }


                                }
                                item {
                                    Column(modifier = Modifier.padding(vertical = 30.dp)) {

                                    }
                                }


                            }
                        }

                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            item {
                                Text(
                                    stringResource(id = R.string.noSubs),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                    }


                } else {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        item {
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

                            TextButton(onClick = { model.loadSubs() }) {
                                Text(
                                    "Erneut Versuchen",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                    }

                }

            }
        }


    }
}


