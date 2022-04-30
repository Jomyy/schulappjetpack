package com.jomy.jetpacklernentutorial.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jomy.jetpacklernentutorial.R
import com.jomy.jetpacklernentutorial.dataclasses.SubData

@Composable
fun SubCard(subData: SubData){


    Surface(shadowElevation = 0.dp, shape = MaterialTheme.shapes.large, tonalElevation = 4.dp, modifier = Modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = MaterialTheme.shapes.large
        )
        .fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if(subData.state == ""){
                Text("Vertreten", style = MaterialTheme.typography.titleLarge)
            }else{
                Text(subData.state, style = MaterialTheme.typography.titleLarge)
            }

            Divider(modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .height(0.5.dp)
                .fillMaxWidth(.5f))
            Text(stringResource(id = R.string.lessontype)+ subData.fach, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
            Divider(modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .height(0.5.dp)
                .fillMaxWidth(.5f))

            Text(stringResource(id = R.string.lesson) + subData.stunde, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)
            Divider(modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .height(0.5.dp)
                .fillMaxWidth(.5f))

            Text(stringResource(id = R.string.teacher) + subData.lehrer, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodySmall)


        }

    }




}