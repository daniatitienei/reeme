package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun CreateReminderScreen() {
    val scrollState = rememberScrollState()

    var titleValue by remember {
        mutableStateOf("")
    }

    var descriptionValue by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create reminder")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(text = "Create")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical
                )
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Text(text = "Details")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = titleValue,
                    onValueChange = { titleValue = it },
                    placeholder = {
                        Text(
                            text = "Enter title",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = descriptionValue,
                    onValueChange = { descriptionValue = it },
                    placeholder = {
                        Text(
                            text = "Enter description",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = false, onCheckedChange = {})
                    Text(text = "Pinned")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            /* TODO Colors */

            Column {
                Text(text = "Date and time")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = descriptionValue,
                    onValueChange = { descriptionValue = it },
                    placeholder = {
                        Text(
                            text = "Select date",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Rounded.CalendarToday, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = descriptionValue,
                    onValueChange = { descriptionValue = it },
                    placeholder = {
                        Text(
                            text = "Select time",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Outlined.Schedule, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                /* TODO Pop select menu */
                OutlinedTextField(
                    value = descriptionValue,
                    onValueChange = { descriptionValue = it },
                    placeholder = {
                        Text(
                            text = "Repeat",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        Icon(Icons.Rounded.Repeat, contentDescription = null)
                    }
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Column {
                Text(text = "Categories")

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    crossAxisSpacing = 10.dp,
                    mainAxisSpacing = 10.dp
                ) {
                    repeat(8) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colors.primary)
                                .padding(horizontal = 15.dp, vertical = 10.dp)
                        ) {
                            Text(text = "Work", color = MaterialTheme.colors.background)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateReminderPreview() {
    ReemeTheme {
        CreateReminderScreen()
    }
}