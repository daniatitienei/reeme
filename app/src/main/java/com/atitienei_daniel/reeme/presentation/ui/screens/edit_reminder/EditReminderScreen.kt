package com.atitienei_daniel.reeme.presentation.ui.screens.edit_reminder

import androidx.compose.runtime.Composable
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.*
import com.atitienei_daniel.reeme.presentation.utils.intToColor
import com.google.accompanist.flowlayout.FlowRow
import androidx.navigation.NavHostController
import com.atitienei_daniel.reeme.data.reminders_db.Reminder
import com.atitienei_daniel.reeme.presentation.ui.utils.ShowDatePicker
import com.atitienei_daniel.reeme.presentation.ui.utils.ShowTimePicker
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun EditReminderScreen(
    navController: NavHostController,
    reminder: Reminder
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val colors = listOf(
        Red900,
        Blue900,
        Yellow900,
        Purple900,
        Orange900,
        Lime900,
        Pink900
    )

    val categories = listOf(
        "Work",
        "Programming",
        "Life",
        "Date",
        "Trip"
    )

    val selectedCategories = remember {
        mutableStateListOf<Int>()
    }

    var selectedColorIndex by remember {
        mutableStateOf(colors.indexOf(intToColor(reminder.color)))
    }

    var title by remember {
        mutableStateOf(reminder.title)
    }

    var description by remember {
        mutableStateOf(reminder.description)
    }

    var isPinned by remember {
        mutableStateOf(reminder.isPinned)
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
        mutableStateOf(reminder.repeat)
    }

    var showRepeatDropdownMenu by remember {
        mutableStateOf(false)
    }

    var year by remember {
        mutableStateOf<Int?>(null)
    }

    var month by remember {
        mutableStateOf<Int?>(null)
    }

    var day by remember {
        mutableStateOf<Int?>(null)
    }

    var hours by remember {
        mutableStateOf<Int?>(null)
    }

    var minutes by remember {
        mutableStateOf<Int?>(null)
    }


    var isDone by remember { mutableStateOf(reminder.isDone) }

    if (showDatePicker)
        ShowDatePicker(
            context = context,
            onDatePicked = { newDate, newYear, newMonth, newDayOfMonth ->
                date = newDate
                year = newYear
                month = newMonth
                day = newDayOfMonth

                showDatePicker = false
                showTimePicker = true
            },
            onDismissRequest = {
                showDatePicker = false
            }
        )

    if (showTimePicker)
        ShowTimePicker(
            context = context,
            onTimePicked = { newTime, hrs, mins ->
                time = newTime
                hours = hrs
                minutes = mins

                val calendar = Calendar.getInstance()
                calendar.set(year!!, month!!, day!!, hours!!, minutes!!)

                showTimePicker = false
            },
            onDismissRequest = {
                showTimePicker = false
            }
        )

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            ) {
                Text(
                    text = "Edit reminder",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.background
            ) {
                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary.copy(0.6f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Close", color = MaterialTheme.colors.primary.copy(0.6f))
                }

                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Rounded.Save,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Save",
                        color = MaterialTheme.colors.primary
                    )
                }

                TextButton(
                    onClick = { isDone = !isDone },
                    modifier = Modifier.weight(1f)
                ) {
                    Crossfade(targetState = isDone) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                        ) {
                            Icon(
                                if (!it) Icons.Rounded.DoneAll else Icons.Rounded.RemoveDone,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (!it) "Done" else "Undone",
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
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

                DetailsOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter title",

                    )

                Spacer(modifier = Modifier.height(10.dp))

                DetailsOutlinedTextField(
                    value = description ?: "",
                    onValueChange = { description = it },
                    placeholder = "Enter description"
                )

                Spacer(modifier = Modifier.height(5.dp))

                Pinned(
                    isPinned = isPinned,
                    onCheckedChange = { isPinned = it }
                )
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

                OutlinedPicker(
                    value = if (date.isNotEmpty() && time.isNotEmpty()) "On $date, at $time" else "",
                    placeholder = "Select date and time",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column {
                    OutlinedPicker(
                        value = when (repeat) {
                            RepeatType.ONCE.ordinal -> "Once"
                            RepeatType.DAILY.ordinal -> "Daily"
                            RepeatType.WEEKLY.ordinal -> "Weekly"
                            RepeatType.MONTHLY.ordinal -> "Monthly"
                            RepeatType.YEARLY.ordinal -> "Yearly"
                            else -> ""
                        },
                        placeholder = "Repeat",
                        trailingIcon = Icons.Outlined.Repeat,
                        onClick = { showRepeatDropdownMenu = true }
                    )

                    DropdownMenu(
                        expanded = showRepeatDropdownMenu,
                        onDismissRequest = { showRepeatDropdownMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            repeat = RepeatType.ONCE.ordinal
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Once")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = RepeatType.DAILY.ordinal
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Daily")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = RepeatType.WEEKLY.ordinal
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Weekly")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = RepeatType.MONTHLY.ordinal
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Monthly")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = RepeatType.YEARLY.ordinal
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Yearly")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Categories(
                categories = categories,
                selectedCategories = selectedCategories,
                onCategoryClick = { index, isSelected ->
                    if (isSelected)
                        selectedCategories.remove(index)
                    else
                        selectedCategories.add(index)
                },
                onCreateCategoryClick = { /*TODO*/ }
            )
        }
    }
}

private enum class RepeatType {
    ONCE,
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
    UNSELECTED
}

@ExperimentalMaterialApi
@Composable
private fun Categories(
    categories: List<String>,
    selectedCategories: List<Int>,
    onCategoryClick: (index: Int, isSelected: Boolean) -> Unit,
    onCreateCategoryClick: () -> Unit
) {
    Column {
        Text(text = "Categories")

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            crossAxisSpacing = 10.dp,
            mainAxisSpacing = 10.dp
        ) {
            repeat(categories.size) { index ->
                val isSelected = selectedCategories.contains(index)

                val backgroundColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background)

                val textColor by animateColorAsState(targetValue = if (isSelected) MaterialTheme.colors.background else MaterialTheme.colors.primary)

                if (index != categories.size - 1)
                    Card(
                        onClick = {
                            onCategoryClick(index, isSelected)
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(backgroundColor)
                                .padding(horizontal = 15.dp, vertical = 5.dp),
                        ) {
                            Text(
                                text = categories[index],
                                color = textColor
                            )
                        }
                    }
                else
                    Card(
                        onClick = onCreateCategoryClick
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colors.background)
                                .padding(horizontal = 15.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Create new category",
                                color = textColor
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun Pinned(
    isPinned: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.clickable {
            onCheckedChange(!isPinned)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isPinned,
            onCheckedChange = onCheckedChange
        )
        Text(text = "Pinned")
    }
}

@Composable
private fun DetailsOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorMessage: String? = null
) {
    Column(
        modifier = Modifier.animateContentSize()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                )
            },
            textStyle = MaterialTheme.typography.body2,
            isError = !errorMessage.isNullOrEmpty()
        )

        if (!errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                fontSize = MaterialTheme.typography.caption.fontSize,
                color = Red900
            )
        }
    }
}

@Composable
private fun OutlinedPicker(
    value: String,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = { },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
            )
        },
        textStyle = MaterialTheme.typography.body2,
        trailingIcon = {
            val color by animateColorAsState(
                targetValue = if (value.isNotEmpty()) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
                    alpha = 0.5f
                )
            )
            Icon(trailingIcon, contentDescription = null, tint = color)
        },
        enabled = false,
        modifier = Modifier.clickable {
            onClick()
        }
    )
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