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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodCard(foodData: FoodDay) {
    val ctx = LocalContext.current
    var contentVisible by remember {
        mutableStateOf(
            SettingsUtil.readSetting(
                foodData.day + "_open",
                context = ctx
            ).toBoolean()
        )
    }


    Card(

        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth(),
        onClick = {
            contentVisible = !contentVisible

            SettingsUtil.writeSetting(
                foodData.day + "_open",
                context = ctx,
                newSetting = contentVisible.toString()
            )
        },
        colors = CardDefaults.outlinedCardColors()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    foodData.day, modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .weight(1f),
                    fontSize = 19.sp
                )



                IconButton(onClick = {
                    contentVisible = !contentVisible
                    SettingsUtil.writeSetting(
                        foodData.day + "_open",
                        context = ctx,
                        newSetting = contentVisible.toString()
                    )
                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Crossfade(targetState = contentVisible) {
                        if (it) {
                            Icon(Icons.Rounded.ArrowDropUp, "Arrow")
                        } else {
                            Icon(Icons.Rounded.ArrowDropDown, "Arrow")
                        }
                    }


                }
            }

            AnimatedVisibility(
                visible = contentVisible,
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

                Column(

                    horizontalAlignment = Alignment.Start
                ) {
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

    }


}