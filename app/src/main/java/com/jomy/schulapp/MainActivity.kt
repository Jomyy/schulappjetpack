package com.jomy.schulapp


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jomy.schulapp.pages.*
import com.jomy.schulapp.ui.theme.SchulAppTheme
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.WorkerUtil


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
        val subsNextModel: SubsNextPageViewModel by viewModels()
        model.setKlasse(
            SettingsUtil.readSetting("selected_class", context = applicationContext),
            context = applicationContext
        )
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



            SchulAppTheme {
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
                                SubsPage(subsModel, model)
                                foodSelected.value = false
                                subsSelected.value = true
                                subsNextSelected.value = false

                            }
                            composable("subsnextpage") {
                                SubsNextPage(subsNextModel, model)
                                foodSelected.value = false
                                subsSelected.value = false
                                subsNextSelected.value = true

                            }


                        }
                    }

                }, topBar = {
                    Surface(shadowElevation = 4.dp, tonalElevation = 4.dp) {
                        CenterAlignedTopAppBar(
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

    private val _selectedKlasse = mutableStateOf("")
    val selectedKlasse: String get() = _selectedKlasse.value
    fun setKlasse(newKlasse: String, context: Context) {
        _selectedKlasse.value = newKlasse
        SettingsUtil.writeSetting("selected_class", newKlasse, context)

    }

}






