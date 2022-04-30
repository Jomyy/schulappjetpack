package com.jomy.jetpacklernentutorial.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun SelectorDialog(
    onDismiss: () -> Unit,
    onNegativeClick: () -> Unit,
    onPositiveClick: (newKlasse: String) -> Unit,
    klassen: List<String>
) {


    Dialog(onDismissRequest = onDismiss) {

        Surface(
            tonalElevation = 10.dp,
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(modifier = Modifier.padding(0.dp)) {

                // Color Selection

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Klassenauswahl", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 10.dp))
                    if(klassen.isEmpty()){
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Huch... Solch Eine Lehre!")
                        }
                    }else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(600.dp), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items(klassen) { klassetext ->
                                OutlinedButton(onClick = {

                                    onPositiveClick(klassetext)
                                }, modifier = Modifier.fillMaxWidth(.8f)) {
                                    Text(klassetext)
                                }
                            }
                            item{
                                Divider(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .height(0.dp)
                                )
                            }
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary, modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        ) {
                            TextButton(onClick = { onPositiveClick("") },) {
                                Text(
                                    "Abbruch",
                                    textAlign = TextAlign.End,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                    }
                }






            }
        }
    }
}