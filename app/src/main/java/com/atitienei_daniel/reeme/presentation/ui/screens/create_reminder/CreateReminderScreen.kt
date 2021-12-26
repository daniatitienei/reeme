package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.*
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@ExperimentalMaterialApi
@Composable
fun CreateReminderScreen() {
    val scrollState = rememberScrollState()

    val colors = listOf(
        Red900,
        Blue900,
        Yellow900,
        Magenta900,
        Orange900,
        Lime900
    )

    var selectedColorIndex by remember {
        mutableStateOf(0)
    }

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var isPinned by remember {
        mutableStateOf(false)
    }

    var date by remember {
        mutableStateOf("")
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var time by remember {
        mutableStateOf("")
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var repeat by remember {
        mutableStateOf("")
    }

    var showRepeatPicker by remember {
        mutableStateOf(false)
    }

    if (showDatePicker)
        ShowDatePicker(
            context = LocalContext.current,
            onDatePicked = {
                date = it
                showDatePicker = false
            },
            onDismissRequest = {
                showDatePicker = false
            }
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create reminder")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.primary
                        )
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
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Text(text = "Details")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
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
                    value = description,
                    onValueChange = { description = it },
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
                    modifier = Modifier.clickable {
                        isPinned = !isPinned
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPinned,
                        onCheckedChange = {
                            isPinned = it
                        }
                    )
                    Text(text = "Pinned")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SelectReminderCardColor(
                colors = colors,
                selectedColorIndex = selectedColorIndex,
                onClick = { index ->
                    selectedColorIndex = index
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Text(text = "Date and time")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    placeholder = {
                        Text(
                            text = "Select date",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    },
                    textStyle = MaterialTheme.typography.body2,
                    trailingIcon = {
                        val color by animateColorAsState(
                            targetValue = if (date.isNotEmpty()) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                                alpha = 0.5f
                            )
                        )
                        Icon(Icons.Rounded.DateRange, contentDescription = null, tint = color)
                    },
                    enabled = false,
                    modifier = Modifier.clickable {
                        showDatePicker = true
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
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
                    value = description,
                    onValueChange = { description = it },
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

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Text(text = "Categories")

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    crossAxisSpacing = 10.dp,
                    mainAxisSpacing = 10.dp
                ) {
                    repeat(32) {
                        var isSelected by remember {
                            mutableStateOf(false)
                        }

                        val backgroundColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background)

                        val textColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.primary)

                        Card(
                            onClick = {
                                isSelected = !isSelected
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(backgroundColor)
                                    .padding(horizontal = 15.dp, vertical = 5.dp),
                            ) {
                                Text(
                                    text = "Work",
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectReminderCardColor(
    colors: List<Color>,
    selectedColorIndex: Int,
    onClick: (Int) -> Unit
) {
    FlowRow(
        mainAxisSpacing = 15.dp,
        crossAxisSpacing = 10.dp,
    ) {
        repeat(colors.size) { index ->
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors[index])
                    .size(42.dp)
                    .clickable {
                        onClick(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedColorIndex == index)
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colors.primary
                    )
            }
        }
    }
}

@Composable
fun ShowDatePicker(
    context: Context,
    onDatePicked: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, yearValue, monthValue, dayOfMonth ->
            onDatePicked("$dayOfMonth/${monthValue + 1}/$yearValue")
        }, year, month, day
    )

    datePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    datePickerDialog.show()
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun CreateReminderPreview() {
    ReemeTheme {
        CreateReminderScreen()
    }
}