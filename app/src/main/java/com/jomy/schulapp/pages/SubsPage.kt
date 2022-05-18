package com.jomy.schulapp.pages


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalWifiStatusbarConnectedNoInternet4
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jomy.schulapp.R
import com.jomy.schulapp.MainActivityViewModel
import com.jomy.schulapp.components.SelectorDialog
import com.jomy.schulapp.components.SubCard
import com.jomy.schulapp.dataclasses.SubData


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsPage(model: MainActivityViewModel) {
    var showSelector by remember { mutableStateOf(false) }
    val isRefreshing by model.isRefreshing.collectAsState()
    val context = LocalContext.current
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
                if (model.selectedKlasse != "") {
                    Text(stringResource(id = R.string.selectedclass) + model.selectedKlasse)
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
                        model.setKlasse(newKlasse = newklasse, context = context)
                        showSelector = !showSelector

                    },
                    klassen = model.klassenListe,
                    oldSelection = model.selectedKlasse
                )
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { model.refresh() },
            ) {
                if (model.errorMessage.isEmpty()) {
                    if (model.subs.isNotEmpty()) {
                        if (model.selectedKlasse == "") {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    stringResource(id = R.string.plsselectclass),
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(it),
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


                            }
                        }

                    } else if (!model.isRefreshing.collectAsState().value) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(id = R.string.noSubs),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }


                } else {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
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


    }
}


