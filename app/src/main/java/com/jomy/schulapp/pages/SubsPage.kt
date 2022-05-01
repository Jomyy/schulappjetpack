package com.jomy.schulapp.pages


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.jetpacklernentutorial.R
import com.jomy.schulapp.MainActivityViewModel
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.components.SelectorDialog
import com.jomy.schulapp.components.SubCard
import com.jomy.schulapp.dataclasses.SubData
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubsPage(model: SubsPageViewModel,mainModel: MainActivityViewModel) {
    var showSelector by remember { mutableStateOf(false) }

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
                if(mainModel.selectedKlasse != ""){
                    Text(stringResource(id = R.string.selectedclass) + mainModel.selectedKlasse)
                }else{
                    Text(stringResource(id = R.string.selectclass))
                }

            }
        }

    }) {

        LaunchedEffect(Unit, block = {
            model.loadSubs()
        })
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
                        mainModel.setKlasse(newKlasse = newklasse)
                        showSelector = !showSelector

                    },
                    klassen = model.klassenListe
                )
            }
            if (model.errorMessage.isEmpty()) {
                if(model.subs.isNotEmpty()){
                    if(mainModel.selectedKlasse == ""){
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(stringResource(id = R.string.plsselectclass), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                        }
                    }else{
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
                            items(model.subs) { sub ->

                                if (sub[0] == mainModel.selectedKlasse) {
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
                            item {
                                Divider(
                                    modifier = Modifier
                                        .padding(26.dp)
                                        .height(0.dp)
                                )
                            }

                        }
                    }

                }else{
                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(stringResource(id = R.string.noSubs), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                    }
                }


            } else {
                Text(stringResource(id = R.string.serverNotOn))
            }

        }


    }
}



class SubsPageViewModel : ViewModel() {
    private val _subs = mutableStateListOf<List<String>>()
    val subs: List<List<String>> get() = _subs

    private val _klassenListe = mutableStateListOf<String>()
    val klassenListe: List<String> get() = _klassenListe
    var errorMessage: String by mutableStateOf("")
    fun loadSubs() {

        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                _subs.clear()
                _subs.addAll(apiService.getSubs())
                _klassenListe.clear()
                var prevKlasse = ""
                _subs.forEach { stundelist ->
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
}