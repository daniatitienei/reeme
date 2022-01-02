package com.atitienei_daniel.reeme.ui.screens.edit_reminder

import android.util.Log
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
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.ui.screens.utils.ShowDatePicker
import com.atitienei_daniel.reeme.ui.screens.utils.ShowTimePicker
import com.atitienei_daniel.reeme.ui.theme.*
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.dateToString
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collect
import java.util.*

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun EditReminderScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: EditReminderViewModel = hiltViewModel()
) {
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    var showRepeatDropdownMenu by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.AlertDialog -> {
                    showDatePicker = event.isOpen
                }
                else -> Unit
            }
        }
    }

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

    var selectedColorIndex by remember {
        mutableStateOf(colors.indexOf(viewModel.color))
    }

    viewModel.color?.let {
        selectedColorIndex = colors.indexOf(viewModel.color)
    }

    if (showDatePicker)
        ShowDatePicker(
            context = context,
            onDatePicked = { newDate, newYear, newMonth, newDayOfMonth ->
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
                hours = hrs
                minutes = mins

                viewModel.date.set(year!!, month!!, day!!, hours!!, minutes!!)

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
                    onClick = { viewModel.onEvent(EditReminderEvents.OnCloseClick) },
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
                    onClick = {
                        viewModel.onEvent(EditReminderEvents.OnSaveClick)
                    },
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
                    onClick = { viewModel.onEvent(EditReminderEvents.OnDoneClick) },
                    modifier = Modifier.weight(1f)
                ) {
                    Crossfade(targetState = viewModel.isDone) { isDone ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                        ) {
                            Icon(
                                if (!isDone) Icons.Rounded.DoneAll else Icons.Rounded.RemoveDone,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (!isDone) "Done" else "Undone",
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
                    value = viewModel.title,
                    onValueChange = { viewModel.title = it },
                    placeholder = "Enter title",

                    )

                Spacer(modifier = Modifier.height(10.dp))

                DetailsOutlinedTextField(
                    value = viewModel.description ?: "",
                    onValueChange = { viewModel.description = it },
                    placeholder = "Enter description"
                )

                Spacer(modifier = Modifier.height(5.dp))

                Pinned(
                    isPinned = viewModel.isPinned,
                    onCheckedChange = { viewModel.isPinned = it }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SelectReminderCardColor(
                colors = colors,
                selectedColorIndex = selectedColorIndex,
                onClick = { index ->
                    selectedColorIndex = index
                    viewModel.color = colors[selectedColorIndex]
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column {
                Text(text = "Date and time")

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedPicker(
                    value = "On " +
                            "${viewModel.date.time.dateToString("dd/MM/yyyy")}," +
                            " at ${viewModel.date.time.dateToString("HH:mm a")}",
                    placeholder = "Select date and time",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Column {
                    OutlinedPicker(
                        value = when (viewModel.repeat) {
                            ReminderRepeatTypes.ONCE -> "Once"
                            ReminderRepeatTypes.DAILY -> "Daily"
                            ReminderRepeatTypes.WEEKLY -> "Weekly"
                            ReminderRepeatTypes.MONTHLY -> "Monthly"
                            ReminderRepeatTypes.YEARLY -> "Yearly"
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
                            viewModel.repeat = ReminderRepeatTypes.ONCE
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Once")
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.repeat = ReminderRepeatTypes.DAILY
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Daily")
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.repeat = ReminderRepeatTypes.WEEKLY
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Weekly")
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.repeat = ReminderRepeatTypes.MONTHLY
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Monthly")
                        }

                        DropdownMenuItem(onClick = {
                            viewModel.repeat = ReminderRepeatTypes.YEARLY
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
                selectedCategories = viewModel.selectedCategories,
                onCategoryClick = { index, isSelected ->
                    if (isSelected)
                        viewModel.selectedCategories.remove(categories[index])
                    else
                        viewModel.selectedCategories.add(categories[index])
                },
                onCreateCategoryClick = { /*TODO*/ }
            )
        }
    }
}

@Composable
private fun RepeatDropDown(
    repeat: ReminderRepeatTypes,
    onRepeatValueChange: (ReminderRepeatTypes) -> Unit,
    showRepeatDropdownMenu: Boolean,
    toggleDropDown: (Boolean) -> Unit
) {
    Column {
        OutlinedPicker(
            value = when (repeat) {
                ReminderRepeatTypes.ONCE -> "Once"
                ReminderRepeatTypes.DAILY -> "Daily"
                ReminderRepeatTypes.WEEKLY -> "Weekly"
                ReminderRepeatTypes.MONTHLY -> "Monthly"
                ReminderRepeatTypes.YEARLY -> "Yearly"
                else -> ""
            },
            placeholder = "Repeat",
            trailingIcon = Icons.Outlined.Repeat,
            onClick = { toggleDropDown(true) }
        )

        DropdownMenu(
            expanded = showRepeatDropdownMenu,
            onDismissRequest = { toggleDropDown(false) }
        ) {
            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.ONCE)
                toggleDropDown(false)
            }) {
                Text(text = "Once")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.DAILY)
                toggleDropDown(false)
            }) {
                Text(text = "Daily")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.WEEKLY)
                toggleDropDown(false)
            }) {
                Text(text = "Weekly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.MONTHLY)
                toggleDropDown(false)
            }) {
                Text(text = "Monthly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.YEARLY)
                toggleDropDown(false)
            }) {
                Text(text = "Yearly")
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Categories(
    categories: List<String>,
    selectedCategories: List<String>,
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
                val isSelected = selectedCategories.contains(categories[index])

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