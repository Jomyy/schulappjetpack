package com.jomy.schulapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.api.APIService
import com.jomy.schulapp.components.PreferencesListSelector
import com.jomy.schulapp.components.PreferencesSwitch
import com.jomy.schulapp.ui.theme.JetpacklernentutorialTheme
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.WorkerUtil
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {
    @SuppressLint("FlowOperatorInvokedInComposition")
    @OptIn(
        ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
        ExperimentalComposeUiApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: SettingsActivityViewModel by viewModels()

        model.loadClasses()
        model.getNotKlase(context = applicationContext)
        setContent {
            val context = LocalContext.current

            var isNoteEnabled by remember {

                mutableStateOf(
                    SettingsUtil.readSetting("notifications_enabled", context = context).toBoolean()
                )
            }

            JetpacklernentutorialTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Surface(shadowElevation = 4.dp) {
                        CenterAlignedTopAppBar(title = { Text("Settings") }, navigationIcon = {
                            IconButton(onClick = {
                                context.startActivity(Intent(context, MainActivity::class.java))
                            }) {
                                Icon(Icons.Rounded.ArrowBack, "back")
                            }
                        })
                    }

                }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        PreferencesSwitch(
                            title = "Benachrichtigungen",
                            icon = Icons.Rounded.Notifications,
                            key = "notifications_enabled",
                            onChange = {
                                isNoteEnabled = it
                            }
                        )
                        Divider(
                            Modifier
                                .padding(vertical = 8.5.dp)
                                .padding(horizontal = 0.dp)
                                .height(0.dp), color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        PreferencesListSelector(list = model.allClasses, key = "notification_class",
                            "Klasse Auswählen", isNoteEnabled
                        ) {
                            WorkerUtil.addWorker(context)
                        }

                        Divider(
                            Modifier
                                .padding(vertical = 8.dp)
                                .padding(horizontal = 0.dp)
                                .height(1.dp), color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
            }
        }
    }
}

class SettingsActivityViewModel : ViewModel() {
    private val _allClasses = mutableListOf<String>()
    val allClasses: List<String> get() = _allClasses
    private var errorMessage: String by mutableStateOf("")

    fun loadClasses() {
        viewModelScope.launch {

            val apiService = APIService.getInstance()
            try {
                _allClasses.clear()
                _allClasses.addAll(apiService.getAllClasses())


            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }

        }

    }

    private val _selectedNotKlasse = mutableStateOf("")


    fun getNotKlase(context: Context) {
        _selectedNotKlasse.value = SettingsUtil.readSetting("notification_class", context = context)


    }
}
