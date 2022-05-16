package com.jomy.schulapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jomy.schulapp.util.SettingsUtil


@Composable
fun PreferencesSwitch(
    title: String,
    icon: ImageVector,
    key: String,
    onChange: (newKlasse: Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    val switchON = remember {
        mutableStateOf(
            SettingsUtil.readSetting(key, context = context).toBoolean()
        ) // Initially the switch is ON
    }


    Row(modifier = Modifier
        .height(65.dp)
        .clickable {
            switchON.value = !switchON.value

            SettingsUtil.writeSetting(key, switchON.value.toString(), context = context)
            onChange(switchON.value)
        }
        .padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon, title, modifier = Modifier
                .padding(end = 20.dp)
                .size(30.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title, style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.wrapContentHeight()
            )
        }
        Switch(checked = switchON.value, onCheckedChange = {
            switchON.value = !switchON.value
            SettingsUtil.writeSetting(key, switchON.value.toString(), context = context)
            onChange(switchON.value)
        })

    }


}


