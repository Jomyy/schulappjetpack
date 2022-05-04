package com.jomy.schulapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jamal.composeprefs.ui.ifNotNullThen
import com.jomy.schulapp.util.SettingsUtil

@Composable
fun PreferencesListSelector(list:List<String>,key:String,textToShow:String,isEnabledAll:Boolean = true,onSelected: (newSelection:String)->Unit = {}){
    var showSelector by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var textShow by remember{ mutableStateOf(SettingsUtil.readSetting(key, context = context))}



    OutlinedButton(
        onClick = {
            showSelector = !showSelector


        }, modifier = Modifier
            .fillMaxWidth().padding(horizontal = 20.dp).height(50.dp)
        , colors = ButtonDefaults.elevatedButtonColors(),
        enabled = isEnabledAll!!
    ) {
        if(textShow == ""){
            Text(textToShow)
        }else{
            Text(textShow)
        }
    }
    if (showSelector) {
        SelectorDialog(
            onDismiss = {
                showSelector = !showSelector

            },

            onPositiveClick = { newklasse ->

                SettingsUtil.writeSetting(key,newklasse, context = context);
                showSelector = !showSelector
                textShow = newklasse
                onSelected(newklasse)
            },
            klassen = list,
            oldSelection = textShow

        )
    }
}