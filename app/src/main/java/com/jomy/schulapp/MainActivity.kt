package com.jomy.schulapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.PermContactCalendar
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.PermContactCalendar
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jomy.schulapp.pages.*
import com.jomy.schulapp.ui.theme.SchulAppTheme
import com.jomy.schulapp.util.Keys
import com.jomy.schulapp.util.SharedPrefsUtil
import com.jomy.schulapp.util.WorkerUtil
import com.jomy.schulapp.viewModels.FoodViewModel
import com.jomy.schulapp.viewModels.SubsNextViewModel
import com.jomy.schulapp.viewModels.SubsViewModel
import kotlinx.coroutines.flow.first


class MainActivity : ComponentActivity() {


    @SuppressLint("FlowOperatorInvokedInComposition")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val extras = intent.extras
        var notornot = "foodpage"


        val foodModel: FoodViewModel by viewModels()
        val subsModel: SubsViewModel by viewModels()
        val subsNextModel: SubsNextViewModel by viewModels()


        WindowCompat.setDecorFitsSystemWindows(window, true)


        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val foodSelected = rememberSaveable { mutableStateOf(true) }
            val subsSelected = rememberSaveable { mutableStateOf(false) }
            val subsNextSelected = rememberSaveable { mutableStateOf(false) }

            val systemUiController = rememberSystemUiController()

            LaunchedEffect(key1 = Unit, block = {
                WorkerUtil.addWorker(
                    context,
                    !SharedPrefsUtil.readBooleanSetting(Keys.NOTIFICATIONS_ENABLED, context).first()
                )
                foodModel.loadFood()
                subsModel.loadSubs()
                subsNextModel.loadSubs()


            })


            if (extras != null) {
                if (extras.getString("note") != null) {
                    notornot = extras.getString("note")!!
                }
            }

            SchulAppTheme {
                val topcolor = TopAppBarDefaults.smallTopAppBarColors().containerColor(scrollFraction = 1f)
                val matcolors = MaterialTheme.colorScheme
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = topcolor.value
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
                                Crossfade(targetState = foodSelected.value) {
                                    if (it) {
                                        Icon(
                                            Icons.Default.Fastfood, "Home"
                                        )
                                    } else {
                                        Icon(
                                            Icons.Outlined.Fastfood, "Home"
                                        )
                                    }
                                }

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
                                Crossfade(targetState = subsSelected.value) {
                                    if (it) {
                                        Icon(
                                            Icons.Default.CalendarToday, "Home"
                                        )
                                    } else {
                                        Icon(
                                            Icons.Outlined.CalendarToday, "Home"
                                        )
                                    }
                                }
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
                                Crossfade(targetState = subsNextSelected.value) {
                                    if (it) {
                                        Icon(
                                            Icons.Default.PermContactCalendar, "Home"
                                        )
                                    } else {
                                        Icon(
                                            Icons.Outlined.PermContactCalendar, "Home"
                                        )
                                    }
                                }
                            }, modifier = Modifier.padding(bottom = 0.dp)
                        )

                    }
                }, content = { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = notornot) {
                            composable("foodpage") {
                                FoodPage(foodModel, subsModel, subsNextModel)
                                foodSelected.value = true
                                subsSelected.value = false
                                subsNextSelected.value = false

                            }
                            composable("subspage") {
                                SubsPage(subsModel, foodModel, subsNextModel)
                                foodSelected.value = false
                                subsSelected.value = true
                                subsNextSelected.value = false

                            }
                            composable("subsnextpage") {
                                SubsNextPage(subsNextModel, foodModel, subsModel)
                                foodSelected.value = false
                                subsSelected.value = false
                                subsNextSelected.value = true

                            }


                        }
                    }

                }, topBar = {

                        SmallTopAppBar(
                            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                                canScroll = { true }),
                            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = topcolor.value)
                            ,
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


                }, containerColor = MaterialTheme.colorScheme.surface)

            }

        }
    }

}






