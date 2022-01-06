package com.atitienei_daniel.reeme.ui.screens.edit_reminder

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.RemoveDone
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.ui.screens.edit_reminder.components.Tooltip
import com.atitienei_daniel.reeme.ui.theme.Red900
import com.atitienei_daniel.reeme.ui.theme.ReemeTheme
import com.atitienei_daniel.reeme.ui.utils.Constants
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.components.*
import com.atitienei_daniel.reeme.ui.utils.dateToString
import kotlinx.coroutines.flow.collect

@ExperimentalFoundationApi
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

    var year by remember {
        mutableStateOf<Int?>(null)
    }

    var month by remember {
        mutableStateOf<Int?>(null)
    }

    var dayOfMonth by remember {
        mutableStateOf<Int?>(null)
    }

    viewModel.color?.let {
        selectedColorIndex = colors.indexOf(viewModel.color)
    }

    if (showDatePicker)
        ShowDatePicker(
            context = context,
            onDatePicked = { _, yearValue, monthValue, day ->
                year = yearValue
                month = monthValue
                dayOfMonth = day

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
            onTimePicked = { _, hours, minutes ->
                viewModel.date.set(
                    year!!,
                    month!!,
                    dayOfMonth!!,
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
            newCategoryTitle = newCategoryTitle,
            onValueChange = {
                newCategoryTitle = it
            },
            onSaveClick = {
                categories!!.add(newCategoryTitle)
                newCategoryTitle = ""
                viewModel.onEvent(EditReminderEvents.InsertCategory(categories = categories!!))
            },
            onCancelClick = {
                viewModel.onEvent(EditReminderEvents.DismissCreateCategoryAlert)
            },
            onDismissRequest = {
                viewModel.onEvent(EditReminderEvents.DismissCreateCategoryAlert)
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

                OutlinedTextFieldWithErrorText(
                    value = viewModel.title,
                    onValueChange = { viewModel.title = it },
                    placeholder = "Enter title"
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextFieldWithErrorText(
                    value = viewModel.description ?: "",
                    onValueChange = { viewModel.description = it },
                    placeholder = "Enter description"
                )

                Spacer(modifier = Modifier.height(5.dp))

                Pinned(
                    isPinned = viewModel.isPinned,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(EditReminderEvents.ToggleCheckBox(isChecked = isChecked))
                    }
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


                RepeatDropdown(
                    repeat = viewModel.repeat,
                    onRepeatValueChange = { viewModel.repeat = it },
                    showRepeatDropdownMenu = showRepeatDropdownMenu,
                    toggleDropDown = { isDropdownOpen ->
                        viewModel.onEvent(EditReminderEvents.ToggleDropdown(isDropdownOpen))
                    }
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
                onCreateCategoryClick = {
                    viewModel.onEvent(EditReminderEvents.OpenCreateCategoryAlertDialog)
                }
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun BottomAppBar(
    onEvent: (EditReminderEvents) -> Unit,
    isDone: Boolean,
) {
    val state = remember {
        mutableStateOf(true)
    }

    var howManyTimesDeleteButtonWasClicked by remember {
        mutableStateOf(0)
    }

    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        ) {
            if (howManyTimesDeleteButtonWasClicked == 1)
                Tooltip(expanded = state) {
                    Text(text = "Press one more time to delete")
                }
            DeleteButton(
                onClick = {
                    if (howManyTimesDeleteButtonWasClicked >= 2)
                        onEvent(EditReminderEvents.OnDeleteClick)
                    else howManyTimesDeleteButtonWasClicked++

                    Log.d("delete_button", howManyTimesDeleteButtonWasClicked.toString())
                },
                modifier = Modifier
            )

        }

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

@ExperimentalFoundationApi
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