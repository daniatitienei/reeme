package com.atitienei_daniel.reeme.ui.screens.reminders.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.ui.screens.reminders.RemindersListEvents
import com.atitienei_daniel.reeme.ui.theme.DarkBlue800
import com.atitienei_daniel.reeme.ui.utils.converters.dateToString
import remindersdb.ReminderEntity

@ExperimentalMaterialApi
@Composable
fun ReminderCard(
    reminder: ReminderEntity,
    onEvent: (RemindersListEvents) -> Unit
) {
    Card(
        backgroundColor = reminder.color,
        onClick = {
            onEvent(RemindersListEvents.OnReminderClick(reminder = reminder))
        },
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Justify,
                color = DarkBlue800,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            if (!reminder.description.isNullOrEmpty()) {
                Text(
                    text = reminder.description,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Justify,
                    color = DarkBlue800,
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = DarkBlue800,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = reminder.date.dateToString("dd/MM/yyyy"),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        color = DarkBlue800,
                    )
                    Text(
                        text = reminder.date.dateToString("HH:mm a"),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                        color = DarkBlue800,
                    )
                }
            }
        }
    }
}