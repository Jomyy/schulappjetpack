package com.jomy.schulapp.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jomy.schulapp.R

import com.jomy.schulapp.dataclasses.SubData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCard(subData: SubData) {


    Card(

        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.outlinedCardColors(),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.large
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (subData.state == "") {
                Text("Vertreten", style = MaterialTheme.typography.titleLarge)
            } else {
                Text(subData.state, style = MaterialTheme.typography.titleLarge)
            }

            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.dp)
                    .fillMaxWidth(.1f)
            )
            Text(
                stringResource(id = R.string.lessontype) + subData.fach,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.dp)
                    .fillMaxWidth(.1f)
            )

            Text(
                stringResource(id = R.string.lesson) + subData.stunde,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.dp)
                    .fillMaxWidth(.1f)
            )

            Text(
                stringResource(id = R.string.teacher) + subData.lehrer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            Divider(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp)
                    .height(0.dp)
                    .fillMaxWidth(.1f)
            )
            Text(
                stringResource(id = R.string.room) + subData.raum,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }


}