package com.jomy.schulapp.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jomy.schulapp.util.SettingsUtil


@Composable
fun PreferencesListSelector(
    list: List<String>,
    key: String,
    textToShow: String,
    isEnabledAll: Boolean = true,
    onSelected: (newSelection: String) -> Unit = {}
) {
    var showSelector by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var textShow by remember { mutableStateOf(SettingsUtil.readSetting(key, context = context)) }



    OutlinedButton(
        onClick = {
            showSelector = !showSelector


        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(60.dp),
        colors = ButtonDefaults.elevatedButtonColors(),
        enabled = isEnabledAll
    ) {
        if (textShow == "") {
            Text(textToShow)
        } else {
            Text(textShow)
        }
    }

    if (showSelector) {
        SelectorDialog(
            onDismiss = {
                showSelector = !showSelector

            },

            onPositiveClick = { newklasse ->

                SettingsUtil.writeSetting(key, newklasse, context = context)
                showSelector = !showSelector
                textShow = newklasse
                onSelected(newklasse)
            },
            klassen = list,
            oldSelection = textShow

        )
    }
}