package com.jomy.schulapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jomy.schulapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorDialog(
    onDismiss: () -> Unit,

    onPositiveClick: (newKlasse: String) -> Unit,
    klassen: List<String>,
    oldSelection: String
) {


    Dialog(onDismissRequest = onDismiss) {
        Scaffold(modifier = Modifier
            .padding(vertical = 40.dp)
            .clip(MaterialTheme.shapes.large),containerColor = MaterialTheme.colorScheme.secondaryContainer,content = {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(12.dp),

                ) {

                Column(modifier = Modifier.padding(0.dp)) {

                    // Color Selection

                    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxHeight(0.9f)) {
                        Text(
                            stringResource(id = R.string.selectclass),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        if (klassen.isEmpty()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(stringResource(id = R.string.empty))
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                , horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(klassen) { klassetext ->
                                    OutlinedButton(onClick = {

                                        onPositiveClick(klassetext)
                                    }, modifier = Modifier.fillMaxWidth(.8f)) {
                                        Text(klassetext)
                                    }
                                }
                                item {
                                    Divider(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .height(0.dp)
                                    )
                                }
                            }

                        }




                    }


                }
            }
        }, bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceTint, modifier = Modifier
                    .fillMaxWidth()
                    .padding()


            ) {
                TextButton(onClick = { onPositiveClick(oldSelection) },modifier = Modifier.height(72.dp)) {
                    Text(
                        stringResource(id = R.string.cancel),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        })

    }
}