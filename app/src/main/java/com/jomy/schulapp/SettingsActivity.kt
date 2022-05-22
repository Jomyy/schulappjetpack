package com.jomy.schulapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jomy.schulapp.components.PrefListSelectorViewModel
import com.jomy.schulapp.components.PreferencesListSelector
import com.jomy.schulapp.components.PreferencesSwitch
import com.jomy.schulapp.components.PreferencesSwitchViewModel
import com.jomy.schulapp.ui.theme.SchulAppTheme
import com.jomy.schulapp.util.Keys
import com.jomy.schulapp.viewModels.SettingsViewModel


class SettingsActivity : ComponentActivity() {
    @SuppressLint("FlowOperatorInvokedInComposition")
    @OptIn(
        ExperimentalMaterial3Api::class
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: SettingsViewModel by viewModels()
        val notificationSettingModel: PreferencesSwitchViewModel by viewModels()
        val notificationKlasseModel: PrefListSelectorViewModel by viewModels()
        model.loadClasses()

        setContent {

            val systemUiController = rememberSystemUiController()


            val context = LocalContext.current
            LaunchedEffect(key1 = Unit, block = {
                model.loadSettings(context)
            })


            SchulAppTheme {
                val matcolors = MaterialTheme.colorScheme
                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = matcolors.background
                    )
                    systemUiController.setNavigationBarColor(
                        color = matcolors.background
                    )
                }
                // A surface container using the 'background' color from the theme
                Scaffold(topBar = {
                    Surface(shadowElevation = 4.dp) {
                        CenterAlignedTopAppBar(
                            title = { Text(stringResource(id = R.string.settings_activity_title)) },
                            navigationIcon = {
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
                            title = stringResource(id = R.string.pref_note),
                            icon = Icons.Rounded.Notifications,
                            key = Keys.NOTIFICATIONS_ENABLED,
                            onChange = {
                                model.setNotificationActive(context)
                            },
                            notificationSettingModel
                        )
                        Divider(
                            Modifier
                                .padding(vertical = 4.dp)
                                .padding(horizontal = 0.dp)
                                .height(0.dp), color = MaterialTheme.colorScheme.inverseOnSurface
                        )
                        PreferencesListSelector(
                            list = model.allClasses, key = Keys.SELECTED_NOTIFICATION_KLASSE,
                            stringResource(id = R.string.selectclass), model.isNoteEnabled.value,
                            model = notificationKlasseModel,
                            onSelected = {

                            }
                        )

                        Divider(
                            Modifier
                                .padding(vertical = 5.dp)
                                .padding(horizontal = 0.dp)
                                .height(1.dp), color = MaterialTheme.colorScheme.inverseOnSurface
                        )

                    }
                }
            }
        }
    }
}


