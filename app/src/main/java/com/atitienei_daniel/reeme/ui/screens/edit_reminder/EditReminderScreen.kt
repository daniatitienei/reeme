package com.atitienei_daniel.reeme.ui.screens.edit_reminder

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
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.ui.screens.utils.ShowDatePicker
import com.atitienei_daniel.reeme.ui.screens.utils.ShowTimePicker
import com.atitienei_daniel.reeme.ui.theme.*
import com.atitienei_daniel.reeme.ui.utils.Constants
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.dateToString
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun EditReminderScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: EditReminderViewModel = hiltViewModel(),
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

    var isCreateCategoryDialogOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.AlertDialog -> {
                    isCreateCategoryDialogOpen = event.isOpen
                }
                is UiEvent.DatePicker -> {
                    showDatePicker = event.isOpen
                }
                is UiEvent.TimePicker -> {
                    showTimePicker = event.isOpen
                }
                is UiEvent.Dropdown -> {
                    showRepeatDropdownMenu = event.isOpen
                }
                else -> Unit
            }
        }
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val colors = Constants.colors

    val categories by viewModel.getCategories().collectAsState(initial = mutableListOf())

    var selectedColorIndex by remember {
        mutableStateOf(colors.indexOf(viewModel.color))
    }

    var newCategoryTitle by remember {
        mutableStateOf("")
    }

    viewModel.color?.let {
        selectedColorIndex = colors.indexOf(viewModel.color)
    }

    if (showDatePicker)
        ShowDatePicker(
            context = context,
            onDatePicked = { newDate, year, month, dayOfMonth ->
                viewModel.date.set(year, month, dayOfMonth)

                viewModel.onEvent(EditReminderEvents.DismissDatePicker)
                viewModel.onEvent(EditReminderEvents.OpenTimePicker)
            },
            onDismissRequest = {
                viewModel.onEvent(EditReminderEvents.DismissDatePicker)
            }
        )

    if (showTimePicker)
        ShowTimePicker(
            context = context,
            onTimePicked = { newTime, hours, minutes ->
                viewModel.date.set(
                    viewModel.date.get(1),
                    viewModel.date.get(2),
                    viewModel.date.get(3),
                    hours,
                    minutes
                )

                viewModel.onEvent(EditReminderEvents.DismissTimePicker)
            },
            onDismissRequest = {
                viewModel.onEvent(EditReminderEvents.DismissTimePicker)
            }
        )

    if (isCreateCategoryDialogOpen)
        CreateCategoryAlertDialog(
            onEvent = viewModel::onEvent,
            newCategoryTitle = newCategoryTitle,
            onValueChange = {
                newCategoryTitle = it
            },
            categories = categories!!,
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
                onEvent = { event -> viewModel.onEvent(event = event) },
                isDone = viewModel.isDone
            )
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
                    placeholder = "Enter title"
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
                    onEvent = viewModel::onEvent
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
                    onClick = { viewModel.onEvent(EditReminderEvents.OpenDatePicker) }
                )

                Spacer(modifier = Modifier.height(10.dp))


                RepeatDropDown(
                    repeat = viewModel.repeat,
                    onRepeatValueChange = { viewModel.repeat = it },
                    showRepeatDropdownMenu = showRepeatDropdownMenu,
                    onEvent = viewModel::onEvent,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Categories(
                categories = categories!!,
                selectedCategories = viewModel.selectedCategories,
                onCategoryClick = { index, isSelected ->
                    if (isSelected)
                        viewModel.selectedCategories.remove(categories!![index])
                    else
                        viewModel.selectedCategories.add(categories!![index])
                },
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun CreateCategoryAlertDialog(
    onEvent: (EditReminderEvents) -> Unit,
    newCategoryTitle: String,
    onValueChange: (String) -> Unit,
    categories: MutableList<String>,
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(EditReminderEvents.DismissCreateCategoryAlert)
        },
        buttons = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(text = "Create reminder")
                }

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = newCategoryTitle,
                    onValueChange = onValueChange,
                    placeholder = {
                        Text(
                            text = "Category name",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = {
                            onEvent(EditReminderEvents.DismissCreateCategoryAlert)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel", color = Red900)
                    }

                    TextButton(
                        onClick = {
                            categories.add(newCategoryTitle)
                            onValueChange("")
                            onEvent(EditReminderEvents.InsertCategory(categories = categories))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Create", color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    )
}

@Composable
private fun BottomAppBar(
    onEvent: (EditReminderEvents) -> Unit,
    isDone: Boolean,
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
        DeleteButton(
            onClick = { onEvent(EditReminderEvents.OnDeleteClick) },
            modifier = Modifier.weight(1f)
        )

        SaveButton(
            onClick = { onEvent(EditReminderEvents.OnSaveClick) },
            modifier = Modifier.weight(1f)
        )

        DoneButton(
            onClick = { onEvent(EditReminderEvents.OnDoneClick) },
            modifier = Modifier.weight(1f),
            isDone = isDone
        )
    }
}

@Composable
private fun DeleteButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            Icons.Outlined.Delete,
            contentDescription = null,
            tint = Red900
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Delete", color = Red900)
    }
}

@Composable
private fun SaveButton(
    onClick: () -> Unit,
    modifier: Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
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
}

@Composable
private fun DoneButton(
    onClick: () -> Unit,
    modifier: Modifier,
    isDone: Boolean,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Crossfade(targetState = isDone) { isDone ->
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

@Composable
private fun RepeatDropDown(
    repeat: ReminderRepeatTypes,
    onRepeatValueChange: (ReminderRepeatTypes) -> Unit,
    showRepeatDropdownMenu: Boolean,
    onEvent: (EditReminderEvents) -> Unit,
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
            onClick = {
                onEvent(EditReminderEvents.ShowDropdown)
            }
        )

        DropdownMenu(
            expanded = showRepeatDropdownMenu,
            onDismissRequest = { onEvent(EditReminderEvents.CloseDropdown) }
        ) {
            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.ONCE)
                onEvent(EditReminderEvents.CloseDropdown)
            }) {
                Text(text = "Once")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.DAILY)
                onEvent(EditReminderEvents.CloseDropdown)
            }) {
                Text(text = "Daily")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.WEEKLY)
                onEvent(EditReminderEvents.CloseDropdown)
            }) {
                Text(text = "Weekly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.MONTHLY)
                onEvent(EditReminderEvents.CloseDropdown)
            }) {
                Text(text = "Monthly")
            }

            DropdownMenuItem(onClick = {
                onRepeatValueChange(ReminderRepeatTypes.YEARLY)
                onEvent(EditReminderEvents.CloseDropdown)
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
    onEvent: (EditReminderEvents) -> Unit,
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
            }

            Card(
                onClick = { onEvent(EditReminderEvents.OpenCreateCategoryAlertDialog) }
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
                        color = MaterialTheme.colors.primary
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

@Composable
private fun Pinned(
    isPinned: Boolean,
    onEvent: (EditReminderEvents) -> Unit,
) {
    Row(
        modifier = Modifier.clickable {
            onEvent(EditReminderEvents.ToggleCheckBox(isChecked = !isPinned))
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isPinned,
            onCheckedChange = { isChecked ->
                onEvent(EditReminderEvents.ToggleCheckBox(isChecked = isChecked))
            }
        )
        Text(text = "Pinned")
    }
}

@Composable
private fun DetailsOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorMessage: String? = null,
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
                color = Red800
            )
        }
    }
}

@Composable
private fun OutlinedPicker(
    value: String,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
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
    onClick: (Int) -> Unit,
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