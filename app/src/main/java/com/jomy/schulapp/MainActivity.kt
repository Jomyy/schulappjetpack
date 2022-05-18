package com.jomy.schulapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.pages.*
import com.jomy.schulapp.ui.theme.SchulAppTheme
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.WorkerUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {


    @SuppressLint("FlowOperatorInvokedInComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val extras = intent.extras
        var notornot = "foodpage"


        val model: MainActivityViewModel by viewModels()
        val foodModel: FoodPageViewModel by viewModels()
        val subsModel: SubsPageViewModel by viewModels()


        if (extras != null) {
            if (extras.getString("note") != null) {
                notornot = extras.getString("note")!!
            }
            if (extras.getString("klasse") != null) {
                model.setKlasse(extras.getString("klasse")!!, context = applicationContext)
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, true)


        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val foodSelected = rememberSaveable { mutableStateOf(true) }
            val subsSelected = rememberSaveable { mutableStateOf(false) }
            val subsNextSelected = rememberSaveable { mutableStateOf(false) }

            WorkerUtil.addWorker(context)

            val systemUiController = rememberSystemUiController()

            LaunchedEffect(key1 = Unit, block = {
                foodModel.loadFood()
                model.loadSubs()
                model.loadSubsNext()
            })



            SchulAppTheme {
                val matcolors = MaterialTheme.colorScheme
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = Color(
                            ColorUtils.blendARGB(
                                matcolors.surface.toArgb(),
                                matcolors.primary.toArgb(),
                                0.09f
                            )
                        )
                    )
                    systemUiController.setNavigationBarColor(
                        color = Color(
                            ColorUtils.blendARGB(
                                matcolors.surface.toArgb(),
                                matcolors.primary.toArgb(),
                                0.085f
                            )
                        )
                    )
                }
                Scaffold(bottomBar = {
                    NavigationBar(modifier = Modifier.height(80.dp)) {
                        NavigationBarItem(
                            selected = foodSelected.value,
                            onClick =
                            {
                                navController.navigate("foodpage")
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.foodplan),
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Rounded.Fastfood, "Home"
                                )
                            },
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        NavigationBarItem(
                            selected = subsSelected.value,
                            onClick = {
                                navController.navigate("subspage")
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.substitutions),
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Rounded.CalendarToday, "Subs"
                                )
                            },
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        NavigationBarItem(
                            selected = subsNextSelected.value,
                            onClick = {
                                navController.navigate("subsnextpage")
                            },
                            label = {
                                Text(
                                    stringResource(id = R.string.substitutionsnext),
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Center
                                )
                            },
                            icon = {
                                Icon(
                                    Icons.Rounded.PermContactCalendar, "substitutionsnextpage"
                                )
                            }, modifier = Modifier.padding(bottom = 0.dp)
                        )

                    }
                }, content = { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = notornot) {
                            composable("foodpage") {
                                FoodPage(foodModel)
                                foodSelected.value = true
                                subsSelected.value = false
                                subsNextSelected.value = false

                            }
                            composable("subspage") {
                                SubsPage(model)
                                foodSelected.value = false
                                subsSelected.value = true
                                subsNextSelected.value = false

                            }
                            composable("subsnextpage") {
                                SubsNextPage(model)
                                foodSelected.value = false
                                subsSelected.value = false
                                subsNextSelected.value = true

                            }


                        }
                    }

                }, topBar = {
                    Surface(shadowElevation = 4.dp, tonalElevation = 4.dp) {
                        SmallTopAppBar(
                            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                                canScroll = { true }),
                            title = { Text(stringResource(id = R.string.app_name)) },
                            modifier = Modifier.padding(top = 0.dp),
                            actions = {
                                IconButton(onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            SettingsActivity::class.java
                                        )
                                    )
                                }) {
                                    Icon(Icons.Rounded.Settings, "settings")
                                }
                            }
                        )
                    }

                }, containerColor = MaterialTheme.colorScheme.surface)

            }

        }
    }

}


class MainActivityViewModel : ViewModel() {
    //#region selectedKlasse
    private val _selectedKlasse = mutableStateOf("")
    val selectedKlasse: String get() = _selectedKlasse.value
    fun setKlasse(newKlasse: String, context: Context) {
        _selectedKlasse.value = newKlasse
        SettingsUtil.writeSetting("selected_class", newKlasse, context)
        updateTomorrowSelectedSubs()
        updateSelectedSubs()
    }

    //#endregion
    private val _tomorrowSelectedSubs = mutableStateListOf<List<String>>()
    val tomorrowSelectedSubs: List<List<String>> get() = _tomorrowSelectedSubs
    private fun updateTomorrowSelectedSubs() {
        _tomorrowSelectedSubs.clear()
        subsnext.forEach {
            if (it[0] == selectedKlasse) {
                _tomorrowSelectedSubs.add(it)
            }
        }
    }

    private val _selectedSubs = mutableStateListOf<List<String>>()
    val selectedSubs: List<List<String>> get() = selectedSubs
    private fun updateSelectedSubs() {
        _selectedSubs.clear()
        subs.forEach {
            if (it[0] == selectedKlasse) {
                _tomorrowSelectedSubs.add(it)
            }
        }
    }

    //#region tomorrow
    private val _subsnext = mutableStateListOf<List<String>>()
    val subsnext: List<List<String>> get() = _subsnext

    private val _klassenListeNext = mutableStateListOf<String>()
    private val _isRefreshingNext = MutableStateFlow(false)

    val isRefreshingNext: StateFlow<Boolean>
        get() = _isRefreshingNext.asStateFlow()
    val klassenListeNext: List<String> get() = _klassenListeNext


    fun loadSubsNext() {

        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                _subsnext.clear()
                _subsnext.addAll(apiService.getSubsNext())
                _klassenListeNext.clear()
                var prevKlasse = ""
                _subsnext.forEach { stundelist ->
                    if (stundelist[0] != prevKlasse) {
                        _klassenListeNext.add(stundelist[0])
                    }
                    prevKlasse = stundelist[0]
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }
        Log.d("ERRORFETCH", errorMessage)

    }

    fun refreshNext() {
        _isRefreshingNext.value = true
        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                delay(300)
                _subsnext.clear()
                _subsnext.addAll(apiService.getSubsNext())

                _klassenListeNext.clear()

                var prevKlasse = ""
                _subsnext.forEach { stundelist ->
                    if (stundelist[0] != prevKlasse) {
                        _klassenListeNext.add(stundelist[0])
                    }
                    prevKlasse = stundelist[0]
                }
                errorMessage = ""
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
            _isRefreshingNext.value = false
        }
    }

    //#endregion
    private val _subs = mutableStateListOf<List<String>>()
    val subs: List<List<String>> get() = _subs

    private val _klassenListe = mutableStateListOf<String>()
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()
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

    fun refresh() {
        _isRefreshing.value = true
        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                delay(300)
                _subs.clear()
                _subs.addAll(apiService.getSubsNext())

                _klassenListe.clear()

                var prevKlasse = ""
                _subs.forEach { stundelist ->
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






