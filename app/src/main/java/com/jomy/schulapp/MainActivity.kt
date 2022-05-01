package com.jomy.schulapp



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jomy.schulapp.R
import com.jomy.schulapp.pages.*
import com.jomy.schulapp.ui.theme.JetpacklernentutorialTheme


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val model: MainActivityViewModel by viewModels()
        val foodModel: FoodPageViewModel by viewModels()
        val subsModel: SubsPageViewModel by viewModels()
        val subsNextModel: SubsNextPageViewModel by viewModels()

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {

            val navController = rememberNavController()
            val foodSelected = rememberSaveable { mutableStateOf(true) }
            val subsSelected = rememberSaveable { mutableStateOf(false) }
            val subsNextSelected = rememberSaveable { mutableStateOf(false) }


            JetpacklernentutorialTheme {
                Scaffold(bottomBar = {
                    NavigationBar(modifier = Modifier.height(80.dp)) {
                        NavigationBarItem(selected = foodSelected.value,
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
                                foodSelected.value = false
                                subsSelected.value = true
                                subsNextSelected.value = false
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
                                    Icons.Rounded.Coronavirus, "Subs"
                                )
                            },
                            modifier = Modifier.padding(bottom = 0.dp))
                        NavigationBarItem(
                            selected = subsNextSelected.value,
                            onClick = {
                                navController.navigate("subsnextpage")
                                foodSelected.value = false
                                subsSelected.value = false
                                subsNextSelected.value = true
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
                                    Icons.Rounded.Coronavirus, "substitutionsnextpage"
                                )
                            }, modifier = Modifier.padding(bottom = 0.dp)
                        )
                    }
                }, content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = "foodpage") {
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
                            modifier = Modifier.padding(top = 0.dp)
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
    fun setKlasse(newKlasse: String) {
        _selectedKlasse.value = newKlasse
    }
}






