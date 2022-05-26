package com.jomy.schulapp.components

import android.content.Context
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.util.SharedPrefsUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@Composable
fun PreferencesSwitch(
    title: String,
    icon: ImageVector,
    key: String,
    onChange: (newKlasse: Boolean) -> Unit = {},
    model: PreferencesSwitchViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit, block = {
        model.readSetting(key, context)
    })


    Row(modifier = Modifier
        .height(65.dp)
        .clickable {
            model.updateSetting(key, !model.enabled, context)
            onChange(!model.enabled)
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
        Switch(checked = model.enabled, onCheckedChange = {
            model.updateSetting(key, it, context)
            onChange(model.enabled)
        })

    }


}

class PreferencesSwitchViewModel : ViewModel() {
    private val _enabled = mutableStateOf(false)
    val enabled get() = _enabled.value
    fun updateSetting(key: String, newVal: Boolean, context: Context) {
        viewModelScope.launch {
            SharedPrefsUtil.writeBooleanSetting(key, newVal, context = context)
            readSetting(key, context)
        }
    }

    fun readSetting(key: String, context: Context) {
        viewModelScope.launch {
            _enabled.value = SharedPrefsUtil.readBooleanSetting(key, context).first()
        }
    }
}

