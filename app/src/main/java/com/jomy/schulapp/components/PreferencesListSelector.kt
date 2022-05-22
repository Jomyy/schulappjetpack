package com.jomy.schulapp.components


import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.SharedPrefsUtil
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@Composable
fun PreferencesListSelector(
    list: List<String>,
    key: String,
    textToShow: String,
    isEnabledAll: Boolean = true,
    onSelected: (newSelection: String) -> Unit = {},
    model:PrefListSelectorViewModel
) {
    var showSelector by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        model.readValue(key,context)
    })



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
        if (model.selectedValue.value == "") {
            Text(textToShow)
        } else {
            Text(model.selectedValue.value)
        }
    }
    if (showSelector) {
        SelectorDialog(
            onDismiss = {
                showSelector = !showSelector

            },

            onPositiveClick = { newklasse ->

                model.setNewValue(key,newklasse,context)
                showSelector = !showSelector
                onSelected(newklasse)
            },
            klassen = list,
            oldSelection = model.selectedValue.value

        )
    }
}
class PrefListSelectorViewModel : ViewModel(){
    private var _selectedValue = mutableStateOf("")
    val selectedValue get() = _selectedValue
    fun setNewValue(key:String,newVal:String,context: Context){
        viewModelScope.launch {
            SharedPrefsUtil.writeStringSetting(key,newVal,context)
            readValue(key,context)
        }
    }
    fun readValue(key:String,context:Context){
        viewModelScope.launch {
            SharedPrefsUtil.readStringSetting(key,context).collect{
                _selectedValue.value = it
            }
        }
    }
}