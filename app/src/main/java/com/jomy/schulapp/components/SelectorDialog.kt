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
            .fillMaxHeight(0.97f)
            .clip(MaterialTheme.shapes.large),
            containerColor = MaterialTheme.colorScheme.surface,
            content = {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(25.dp),
                    shadowElevation = 0.dp,
                    modifier = Modifier.padding(it)
                ) {

                    Column {

                        // Color Selection

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                stringResource(id = R.string.selectclass),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,

                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 15.dp)
                            )
                            Divider(color = MaterialTheme.colorScheme.outline)
                            if (klassen.isEmpty()) {
                                Column(modifier = Modifier.padding(15.dp)) {
                                    Text(stringResource(id = R.string.empty))
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        ,
                                    horizontalAlignment = Alignment.CenterHorizontally,

                                    ) {
                                    items(klassen) { klassetext ->
                                        OutlinedButton(onClick = {

                                            onPositiveClick(klassetext)
                                        }, modifier = Modifier.fillMaxWidth(.8f)) {
                                            Text(klassetext)
                                        }
                                    }

                                }

                            }


                        }


                    }
                }
            },
            bottomBar = {
                Divider(color = MaterialTheme.colorScheme.outline)
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    TextButton(onClick = { onPositiveClick(oldSelection) }) {
                        Text(
                            stringResource(id = R.string.cancel),
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

            })

    }
}
