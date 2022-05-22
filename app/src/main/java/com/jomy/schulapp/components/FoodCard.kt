package com.jomy.schulapp.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jomy.schulapp.dataclasses.FoodDay
import com.jomy.schulapp.util.SettingsUtil
import com.jomy.schulapp.util.SharedPrefsUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(foodData: FoodDay) {
    val ctx = LocalContext.current

    Card(

        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth(),

        colors = CardDefaults.outlinedCardColors()
    ) {




            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    foodData.day,
                    fontSize = 19.sp
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 0.dp)
                        .height(0.dp)
                        .fillMaxWidth()
                )
                Text(
                    foodData.firstMeal,

                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .height(0.dp)
                        .fillMaxWidth(.5f)
                )

                Text(
                    foodData.secondMeal,

                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .height(0.dp)
                        .fillMaxWidth(.5f)
                )

                Text(
                    foodData.thirdMeal,

                    style = MaterialTheme.typography.bodyMedium
                )
            }



    }


}