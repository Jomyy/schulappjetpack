package com.jomy.schulapp.pages

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.jomy.schulapp.R
import com.jomy.schulapp.MainActivityViewModel
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.components.SelectorDialog
import com.jomy.schulapp.components.SubCard
import com.jomy.schulapp.dataclasses.SubData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsNextPage(model: SubsNextPageViewModel, mainModel: MainActivityViewModel) {
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
                if (mainModel.selectedKlasse != "") {
                    Text(stringResource(id = R.string.selectedclass) + mainModel.selectedKlasse)
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
                        mainModel.setKlasse(newKlasse = newklasse, context = context)
                        showSelector = !showSelector

                    },
                    klassen = model.klassenListe,
                    oldSelection = mainModel.selectedKlasse

                )
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { model.refresh() },
            ) {

                if (model.errorMessage.isEmpty()) {
                    if (model.subsnext.isNotEmpty()) {
                        if (mainModel.selectedKlasse == "") {
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
                                items(model.subsnext, key = { message ->
                                    // Return a stable + unique key for the item
                                    message[0] + message[1] + message[2] + message[3] + message[4] + message[5] + message[6]
                                }) { sub ->

                                    if (sub[0] == mainModel.selectedKlasse) {
                                        Column {
                                            SubCard(
                                                subData = SubData(
                                                    sub[0],
                                                    sub[1],
                                                    sub[2],
                                                    sub[3],
                                                    sub[4],
                                                    sub[5],
                                                    sub[6]
                                                )
                                            )
                                            Divider(
                                                modifier = Modifier
                                                    .padding(7.dp)
                                                    .height(0.dp)
                                            )


                                        }
                                    }


                                }
                                item {
                                    Divider(
                                        modifier = Modifier
                                            .padding(26.dp)
                                            .height(0.dp)
                                    )
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
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
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


class SubsNextPageViewModel : ViewModel() {
    private val _subsnext = mutableStateListOf<List<String>>()
    val subsnext: List<List<String>> get() = _subsnext

    private val _klassenListe = mutableStateListOf<String>()
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
    val klassenListe: List<String> get() = _klassenListe

    var errorMessage: String by mutableStateOf("")
    fun loadSubsNext() {

        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                _subsnext.clear()
                _subsnext.addAll(apiService.getSubsNext())
                _klassenListe.clear()
                var prevKlasse = ""
                _subsnext.forEach { stundelist ->
                    if (stundelist[0] != prevKlasse) {
                        _klassenListe.add(stundelist[0])
                    }
                    prevKlasse = stundelist[0]
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }
        Log.d("ERRORFETCH", errorMessage)

    }

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                delay(300)
                _subsnext.clear()
                _subsnext.addAll(apiService.getSubsNext())

                _klassenListe.clear()

                var prevKlasse = ""
                _subsnext.forEach { stundelist ->
                    if (stundelist[0] != prevKlasse) {
                        _klassenListe.add(stundelist[0])
                    }
                    prevKlasse = stundelist[0]
                }
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
            _isRefreshing.value = false
        }
    }
}