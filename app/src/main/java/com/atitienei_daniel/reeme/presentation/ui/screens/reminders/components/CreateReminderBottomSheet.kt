package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.*
import com.google.accompanist.flowlayout.FlowRow
import java.util.*

@ExperimentalMaterialApi
@Composable
fun CreateReminderBottomSheet(
    onNavigationIconClick: () -> Unit,
    onCreateNewCategoryClick: () -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val colors = listOf(
        Red900,
        Blue900,
        Yellow900,
        Magenta900,
        Orange900,
        Lime900
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

    var showRepeatDropdownMenu by remember {
        mutableStateOf(false)
    }

    if (showDatePicker)
        ShowDatePicker(
            context = context,
            onDatePicked = {
                date = it
                showDatePicker = false
            },
            onDismissRequest = {
                showDatePicker = false
            }
        )

    if (showTimePicker)
        ShowTimePicker(
            context = context,
            onTimePicked = {
                time = it
                showTimePicker = false
            },
            onDismissRequest = {
                showTimePicker = false
            }
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create reminder")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Close",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.rotate(-90f)
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

                DetailsOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Enter title"
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
                    value = date,
                    placeholder = "Select date",
                    trailingIcon = Icons.Rounded.DateRange,
                    onClick = { showDatePicker = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedPicker(
                    value = time,
                    placeholder = "Select time",
                    trailingIcon = Icons.Outlined.Schedule,
                    onClick = { showTimePicker = true }
                )

                Spacer(modifier = Modifier.height(10.dp))

                /* TODO Popup select menu */
                Column {
                    OutlinedPicker(
                        value = repeat,
                        placeholder = "Repeat",
                        trailingIcon = Icons.Outlined.Repeat,
                        onClick = { showRepeatDropdownMenu = true }
                    )

                    DropdownMenu(
                        expanded = showRepeatDropdownMenu,
                        onDismissRequest = { showRepeatDropdownMenu = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            repeat = "Once"
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Once")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = "Daily"
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Daily")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = "Monthly"
                            showRepeatDropdownMenu = false
                        }) {
                            Text(text = "Monthly")
                        }

                        DropdownMenuItem(onClick = {
                            repeat = "Yearly"
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
                onCreateCategoryClick = onCreateNewCategoryClick
            )
        }
    }
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
    placeholder: String
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
        textStyle = MaterialTheme.typography.body2
    )
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

@Composable
fun ShowTimePicker(
    context: Context,
    onTimePicked: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minuteValue ->
            onTimePicked("$hourOfDay:$minuteValue")
        }, hour, minute, false
    )

    timePickerDialog.setOnDismissListener {
        onDismissRequest()
    }

    timePickerDialog.show()
}