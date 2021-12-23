package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.components.StaggeredVerticalGrid

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemindersScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Reminders") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.Search, contentDescription = null)
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Rounded.FilterList, contentDescription = null)
                    }
                },
                backgroundColor = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) { innerPadding ->

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp)
        ) {
            item {
                Text(text = "Upcoming")

                Spacer(modifier = Modifier.height(10.dp))

                StaggeredVerticalGrid {
                    repeat(18) {
                        Box(
                            modifier = Modifier.padding(
                                start = if (it % 2 != 0) 5.dp else 0.dp,
                                end = if (it % 2 != 0) 0.dp else 5.dp,
                                bottom = 10.dp
                            )
                        ) {
                            if (it % 3 != 0)
                                ReminderCard(
                                    color = Color(0xffD2F49B),
                                    title = "Take a pill",
                                    description = "Lorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsum",
                                    time = "Tommorow, 4:20"
                                )
                            else
                                ReminderCard(
                                    color = Color(0xffF49B9B),
                                    title = "Take a pill",
                                    description = "Lorem ipsum lorem ipsum lorem ipsum",
                                    time = "Tommorow, 4:20"
                                )
                        }
                    }
                }
            }
        }

    }
}

@ExperimentalMaterialApi
@Composable
private fun ReminderCard(
    color: Color,
    title: String,
    description: String,
    time: String
) {
    Card(
        backgroundColor = color,
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp)
            ) {
                Text(
                    text = time,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ReminderCardPreview() {
    ReemeTheme {
        ReminderCard(
            color = Color(0xffF49B9B),
            title = "Take a pill",
            description = "Lorem ipsum lorem ipsum lorem ipsum",
            time = "Tommorow, 4:20"
        )
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun RemindersPreview() {
    ReemeTheme {
        RemindersScreen()
    }
}