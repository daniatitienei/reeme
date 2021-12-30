package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import androidx.compose.animation.Crossfade
import androidx.navigation.NavHostController
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.R
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue800
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.components.StaggeredVerticalGrid
import com.atitienei_daniel.reeme.presentation.utils.Screens
import com.atitienei_daniel.reeme.presentation.utils.intToColor
import com.google.accompanist.flowlayout.FlowRow
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RemindersScreen(
    navController: NavHostController,
    viewModel: RemindersViewModel = hiltViewModel(),
    moshi: Moshi
) {
    val state = viewModel.state.value

    val upcomingReminders = state.filter { reminder -> !reminder.isPinned }
    val pinnedReminders = state.filter { reminder -> reminder.isPinned }
    val completedReminders = state.filter { reminder -> reminder.isDone }

    val scrollState = rememberScrollState()

    val scope = rememberCoroutineScope()

    var isFilterOpened by remember {
        mutableStateOf(false)
    }

    var searchBarValue by remember {
        mutableStateOf("")
    }

    val interactionSource = MutableInteractionSource()

    val isFocused by interactionSource.collectIsFocusedAsState()

    val searchIconColor by animateColorAsState(
        targetValue = if (isFocused && searchBarValue.isNotEmpty()) DarkBlue800 else DarkBlue800.copy(
            alpha = 0.5f
        ), animationSpec = tween(300)
    )

    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {
            val rotationAngle by animateFloatAsState(targetValue = if (backdropState.isRevealed) 90f else 0f)

            Crossfade(targetState = backdropState.isConcealed) { isConcealed ->
                if (isConcealed)
                    TopBar(
                        title = "Reminders",
                        onSettingsIconClick = { /*TODO*/ },
                        onSearchIconClick = {
                            scope.launch {
                                backdropState.reveal()
                            }
                        },
                        onFilterIconClick = { isFilterOpened = !isFilterOpened },
                        isFilterOpened = isFilterOpened
                    )
                else
                    SearchTopBar(
                        value = searchBarValue,
                        onValueChange = { searchBarValue = it },
                        onCloseIconClick = {
                            scope.launch {
                                backdropState.conceal()
                            }
                        },
                        onClearTextClick = { searchBarValue = "" },
                        interactionSource = interactionSource,
                        searchIconColor = searchIconColor,
                        angle = rotationAngle
                    )
            }
        },
        backLayerContent = {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(17) {
                    ListItem(
                        text = {
                            Text(
                                text = "Recent search ${it + 1}",
                                style = MaterialTheme.typography.body2
                            )
                        },
                        icon = {
                            Icon(
                                Icons.Rounded.History,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        },
                        modifier = Modifier.clickable { }
                    )
                }
            }
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Screens.CreateReminder.route) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 15.dp)
                        .verticalScroll(scrollState)
                        .animateContentSize()
                ) {
                    Crossfade(targetState = isFilterOpened) {
                        if (it)
                            Column(
                                modifier = Modifier.animateContentSize()
                            ) {

                                Text(text = "Filters")

                                FlowRow(
                                    mainAxisSpacing = 10.dp
                                ) {
                                    repeat(10) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Checkbox(
                                                checked = false,
                                                onCheckedChange = { /*TODO*/ })
                                            Text(
                                                text = "Work $it",
                                                style = MaterialTheme.typography.body2
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(15.dp))
                            }

                    }
                    if (pinnedReminders.isNotEmpty()) {
                        Text(text = "Pinned")

                        Spacer(modifier = Modifier.height(10.dp))

                        StaggeredVerticalGrid {
                            repeat(pinnedReminders.size) {
                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                ) {
                                    ReminderCard(
                                        reminder = pinnedReminders[it],
                                        onClick = {
                                            val jsonAdapter =
                                                moshi.adapter(Reminder::class.java).lenient()
                                            val reminderJson =
                                                jsonAdapter.toJson(pinnedReminders[it])

                                            navController.navigate(
                                                Screens.EditReminder.route.replace(
                                                    "{reminder}",
                                                    reminderJson
                                                )
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    if (upcomingReminders.isNotEmpty()) {
                        Text(text = "Upcoming")

                        Spacer(modifier = Modifier.height(10.dp))

                        StaggeredVerticalGrid {
                            repeat(upcomingReminders.size) {
                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                ) {
                                    ReminderCard(
                                        reminder = upcomingReminders[it],
                                        onClick = {
                                            val jsonAdapter =
                                                moshi.adapter(Reminder::class.java).lenient()
                                            val reminderJson =
                                                jsonAdapter.toJson(upcomingReminders[it])

                                            navController.navigate(
                                                Screens.EditReminder.route.replace(
                                                    "{reminder}",
                                                    reminderJson
                                                )
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (completedReminders.isNotEmpty()) {
                        Text(text = "Completed")

                        Spacer(modifier = Modifier.height(10.dp))

                        StaggeredVerticalGrid {
                            repeat(completedReminders.size) {
                                Box(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                ) {
                                    ReminderCard(
                                        reminder = completedReminders[it],
                                        onClick = {
                                            val jsonAdapter =
                                                moshi.adapter(Reminder::class.java).lenient()
                                            val reminderJson =
                                                jsonAdapter.toJson(completedReminders[it])

                                            navController.navigate(
                                                Screens.EditReminder.route.replace(
                                                    "{reminder}",
                                                    reminderJson
                                                )
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        headerHeight = 0.dp,
        stickyFrontLayer = false,
        gesturesEnabled = false,
        backLayerBackgroundColor = MaterialTheme.colors.background,
    )
}


@Composable
private fun CreateCategoryAlertDialog(
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onCreateCategoryButtonClick: () -> Unit,
    onDone: (KeyboardActionScope) -> Unit,
    categoryTitleValue: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        buttons = {
            Column(
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {
                    Text(text = "Create new category", style = MaterialTheme.typography.h6)
                }

                Spacer(modifier = Modifier.height(15.dp))

                OutlinedTextField(
                    value = categoryTitleValue,
                    onValueChange = onValueChange,
                    placeholder = { Text(text = "Enter title") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = onDone
                    )
                )

                Spacer(modifier = Modifier.height(15.dp))

                Button(
                    onClick = onCreateCategoryButtonClick,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(text = "Create")
                }
            }
        }
    )
}

@Composable
private fun TopBar(
    onSettingsIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onFilterIconClick: () -> Unit,
    isFilterOpened: Boolean,
    title: String
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(onClick = onSearchIconClick) {
                Icon(Icons.Rounded.Search, contentDescription = null)
            }

            IconButton(onClick = onFilterIconClick) {
                Crossfade(targetState = isFilterOpened) { isOpen ->
                    if (!isOpen)
                        Icon(Icons.Rounded.FilterList, contentDescription = null)
                    else
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter_list_off),
                            contentDescription = null
                        )
                }
            }

            IconButton(onClick = onSettingsIconClick) {
                Icon(Icons.Outlined.Settings, contentDescription = null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}

@ExperimentalMaterialApi
@Composable
private fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    onCloseIconClick: () -> Unit,
    onClearTextClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    searchIconColor: Color,
    angle: Float
) {
    val size = remember {
        androidx.compose.animation.core.Animatable(0.6f)
    }

    LaunchedEffect(key1 = true) {
        size.animateTo(0.9f)
    }

    TopAppBar(
        title = {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.body2.fontSize,
                    fontFamily = MaterialTheme.typography.body2.fontFamily,
                    color = MaterialTheme.typography.body2.color
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .animateContentSize(tween(300))
                            .fillMaxWidth(size.value)
                            .background(Color.Black.copy(alpha = 0.07f))
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                            tint = searchIconColor,
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Crossfade(targetState = value.isEmpty()) { isEmpty ->
                                if (isEmpty)
                                    Text(
                                        text = "Search something",
                                        style = MaterialTheme.typography.body2,
                                        color = MaterialTheme.typography.body2.color.copy(
                                            alpha = 0.5f
                                        )
                                    )
                            }

                            innerTextField()

                            Crossfade(targetState = value.isNotEmpty()) { isNotEmpty ->
                                if (isNotEmpty)
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.End)
                                    ) {
                                        Icon(
                                            Icons.Rounded.Close,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(
                                                    CircleShape
                                                )
                                                .clickable { onClearTextClick() }
                                        )
                                    }
                            }
                        }
                    }
                }
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onCloseIconClick,
                modifier = Modifier.rotate(angle + 180f)
            ) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}

@ExperimentalMaterialApi
@Composable
private fun ReminderCard(
    reminder: Reminder,
    onClick: () -> Unit
) {
    Card(
        backgroundColor = intToColor(reminder.color),
        onClick = onClick,
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            if (!reminder.description.isNullOrEmpty()) {
                Text(
                    text = reminder.description,
                    style = MaterialTheme.typography.body2,
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "dd/MM/yyyy",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "HH:mm a",
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
