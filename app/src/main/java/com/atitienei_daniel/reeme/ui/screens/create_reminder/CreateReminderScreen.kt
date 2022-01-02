package com.atitienei_daniel.reeme.ui.screens.create_reminder

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.ui.theme.*
import com.atitienei_daniel.reeme.ui.screens.utils.ShowDatePicker
import com.atitienei_daniel.reeme.ui.screens.utils.ShowTimePicker
import com.atitienei_daniel.reeme.ui.utils.UiEvent
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
    viewModel: CreateReminderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val colors = listOf(
        Red800,
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
        mutableStateOf(ReminderRepeatTypes.UNSELECTED)
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
                    isCreateCategoryDialogOpen = !isCreateCategoryDialogOpen
                }
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                else -> Unit
            }
        }
    }

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

                calendar.set(year!!, month!!, day!!, hours!!, minutes!!)

                showTimePicker = false
            },
            onDismissRequest = {
                showTimePicker = false
            }
        )

    if (isCreateCategoryDialogOpen)
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(
                    CreateReminderEvents.OnCreateCategoryAlertDismiss
                )
            },
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(text = "Create reminder")
                }

                OutlinedTextField(
                    value = newCategoryTitle,
                    onValueChange = { newCategoryTitle = it },
                    placeholder = {
                        Text(
                            text = "Category name",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.typography.body2.color.copy(0.7f)
                        )
                    }
                )

                TextButton(
                    onClick = { /*TODO*/ },
                    enabled = newCategoryTitle.isNotEmpty()
                ) {
                    Text(text = "Save", color = MaterialTheme.colors.primary)
                }
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

                DetailsOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter title",
                    errorMessage = viewModel.titleError.value
                )

                Spacer(modifier = Modifier.height(10.dp))

                DetailsOutlinedTextField(
                    value = description,
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
                    value = if (date.isNotEmpty() && time.isNotEmpty()) "On ${
                        calendar.time.dateToString(
                            "dd/MM/yyyy"
                        )
                    }, at ${calendar.time.dateToString("HH:mm")}" else "",
                    placeholder = "Select date and time",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                RepeatDropDown(
                    repeat = repeat,
                    showRepeatDropdownMenu = showRepeatDropdownMenu,
                    onRepeatValueChange = { repeat = it },
                    toggleDropDown = { showRepeatDropdownMenu = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Categories(
                categories = categories,
                selectedCategories = selectedCategories,
                onCategoryClick = { index, isSelected ->
                    if (isSelected)
                        selectedCategories.remove(categories[index])
                    else
                        selectedCategories.add(categories[index])
                },
                onEvent = viewModel::onEvent
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
    calendar: Calendar
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
    onEvent: (CreateReminderEvents) -> Unit
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
                    CreateNewCategoryButton(onEvent = onEvent)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CreateNewCategoryButton(
    onEvent: (CreateReminderEvents) -> Unit,
) {
    Card(
        onClick = {
            onEvent(CreateReminderEvents.OnCreateCategoryAlertClick)
        }
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