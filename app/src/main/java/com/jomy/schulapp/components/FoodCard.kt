package com.jomy.schulapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jomy.schulapp.dataclasses.FoodDay

@Composable
fun FoodCard(foodData: FoodDay) {


    Surface(
        shadowElevation = 0.dp,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(foodData.day, style = MaterialTheme.typography.titleLarge)
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.5.dp)
                    .fillMaxWidth(.5f)
            )
            Text(
                foodData.firstMeal,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.5.dp)
                    .fillMaxWidth(.5f)
            )

            Text(
                foodData.secondMeal,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.5.dp)
                    .fillMaxWidth(.5f)
            )

            Text(
                foodData.thirdMeal,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )


        }

    }


}