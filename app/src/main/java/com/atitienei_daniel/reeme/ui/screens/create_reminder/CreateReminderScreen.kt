package com.atitienei_daniel.reeme.ui.screens.create_reminder

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.outlined.NotificationsOff
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
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.ui.theme.*
import com.atitienei_daniel.reeme.ui.utils.components.ShowDatePicker
import com.atitienei_daniel.reeme.ui.utils.components.ShowTimePicker
import com.atitienei_daniel.reeme.ui.utils.Constants
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.components.*
import com.atitienei_daniel.reeme.ui.utils.dateToString
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collect
import java.util.*

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun CreateReminderScreen(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    viewModel: CreateReminderViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val colors = Constants.colors

    var isCreateCategoryDialogOpen by remember {
        mutableStateOf(false)
    }

    val selectedCategories = remember {
        mutableStateListOf<String>()
    }

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

    var isDatePickerOpen by remember {
        mutableStateOf(false)
    }

    var time by remember {
        mutableStateOf("")
    }

    var isTimePickerOpen by remember {
        mutableStateOf(false)
    }

    var repeat by remember {
        mutableStateOf(ReminderRepeatTypes.UNSELECTED)
    }

    var showRepeatDropdownMenu by remember {
        mutableStateOf(false)
    }

    var newCategoryTitle by remember {
        mutableStateOf("")
    }

    val calendar by remember {
        mutableStateOf(Calendar.getInstance())
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.AlertDialog -> {
                    isCreateCategoryDialogOpen = event.isOpen
                }
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.DatePicker -> {
                    isDatePickerOpen = event.isOpen
                }
                is UiEvent.TimePicker -> {
                    isTimePickerOpen = event.isOpen
                }
                is UiEvent.CheckBox -> {
                    isPinned = event.isChecked
                }
                is UiEvent.Dropdown -> {
                    showRepeatDropdownMenu = event.isOpen
                }
                else -> Unit
            }
        }
    }

    val categories by viewModel.getCategories().collectAsState(initial = mutableListOf())

    if (isDatePickerOpen)
        ShowDatePicker(
            context = context,
            onDatePicked = { newDate, year, month, dayOfMonth ->
                date = newDate

                calendar.set(year, month, dayOfMonth)

                viewModel.onEvent(CreateReminderEvents.DismissDatePicker)
                viewModel.onEvent(CreateReminderEvents.OpenTimePicker)
            },
            onDismissRequest = {
                viewModel.onEvent(CreateReminderEvents.DismissDatePicker)
            }
        )

    if (isTimePickerOpen)
        ShowTimePicker(
            context = context,
            onTimePicked = { newTime, hours, minutes ->

                time = newTime

                calendar.set(calendar.get(1), calendar.get(2), calendar.get(3), hours, minutes)

                viewModel.onEvent(CreateReminderEvents.DismissTimePicker)
            },
            onDismissRequest = {
                viewModel.onEvent(CreateReminderEvents.DismissTimePicker)
            }
        )

    if (isCreateCategoryDialogOpen)
        CreateCategoryAlertDialog(
            newCategoryTitle = newCategoryTitle,
            onValueChange = {
                newCategoryTitle = it
            },
            onSaveClick = {
                if (newCategoryTitle.isEmpty())
                    return@CreateCategoryAlertDialog

                categories!!.add(newCategoryTitle)
                newCategoryTitle = ""
                viewModel.onEvent(CreateReminderEvents.InsertCategory(categories = categories!!))
            },
            onCancelClick = {
                viewModel.onEvent(CreateReminderEvents.DismissCreateCategoryAlert)
            },
            onDismissRequest = {
                viewModel.onEvent(CreateReminderEvents.DismissCreateCategoryAlert)
            }
        )

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.background,
                elevation = 0.dp
            ) {
                Text(
                    text = "Create reminder",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            BottomBar(
                onPopBackStack = onPopBackStack,
                onEvent = viewModel::onEvent,
                title = title,
                description = description,
                color = colors[selectedColorIndex],
                isPinned = isPinned,
                repeat = repeat,
                selectedCategories = selectedCategories,
                calendar = calendar
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
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter title",
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextFieldWithErrorText(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Enter description"
                )

                Spacer(modifier = Modifier.height(5.dp))

                Pinned(
                    isPinned = isPinned,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(CreateReminderEvents.ToggleCheckBox(isChecked = isChecked))
                    }
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
                    value = if (date.isNotEmpty() && time.isNotEmpty()) "On ${
                        calendar.time.dateToString(
                            "dd/MM/yyyy"
                        )
                    }, at ${calendar.time.dateToString("HH:mm")}" else "",
                    placeholder = "Select date and time",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = { viewModel.onEvent(CreateReminderEvents.OpenDatePicker) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                RepeatDropdown(
                    repeat = repeat,
                    showRepeatDropdownMenu = showRepeatDropdownMenu,
                    onRepeatValueChange = { repeat = it },
                    toggleDropDown = { viewModel.onEvent(CreateReminderEvents.ToggleDropdown(isOpen = it)) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Crossfade(targetState = repeat) {
                    if (it == ReminderRepeatTypes.UNSELECTED) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Outlined.NotificationsOff,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "You won't be notified.",
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Categories(
                categories = categories!!,
                selectedCategories = selectedCategories,
                onCategoryClick = { index, isSelected ->
                    if (isSelected)
                        selectedCategories.remove(categories!![index])
                    else
                        selectedCategories.add(categories!![index])
                },
                onCreateCategoryClick = {
                    viewModel.onEvent(
                        CreateReminderEvents.OpenCreateCategoryAlert
                    )
                }
            )
        }
    }
}

@Composable
private fun BottomBar(
    onPopBackStack: (UiEvent.PopBackStack) -> Unit,
    onEvent: (CreateReminderEvents) -> Unit,
    title: String,
    description: String,
    color: Color,
    isPinned: Boolean,
    repeat: ReminderRepeatTypes,
    selectedCategories: List<String>,
    calendar: Calendar,
) {
    BottomAppBar(
        backgroundColor = MaterialTheme.colors.background
    ) {
        TextButton(
            onClick = { onPopBackStack(UiEvent.PopBackStack) },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colors.primary.copy(0.6f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cancel", color = MaterialTheme.colors.primary.copy(0.6f))
        }

        TextButton(
            onClick = {
                onEvent(
                    CreateReminderEvents.OnCreateReminderClick(
                        reminder = Reminder(
                            title = title,
                            description = description,
                            color = color,
                            isPinned = isPinned,
                            repeat = repeat,
                            categories = selectedCategories,
                            isDone = false,
                            date = calendar
                        )
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Rounded.Add,
                contentDescription = null,
                tint = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Create", color = MaterialTheme.colors.primary)
        }
    }
}

@Composable
fun OutlinedPicker(
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