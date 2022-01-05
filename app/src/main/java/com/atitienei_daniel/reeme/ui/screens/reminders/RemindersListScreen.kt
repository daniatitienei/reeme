package com.atitienei_daniel.reeme.ui.screens.reminders

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.R
import com.atitienei_daniel.reeme.ui.screens.reminders.components.ReminderCard
import com.atitienei_daniel.reeme.ui.theme.DarkBlue800
import com.atitienei_daniel.reeme.ui.screens.reminders.components.StaggeredVerticalGrid
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import remindersdb.ReminderEntity

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RemindersListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: RemindersListViewModel = hiltViewModel(),
) {
    val state = viewModel.reminders.collectAsState(initial = emptyList()).value

    val backdropState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val categories by viewModel.categories.collectAsState(initial = mutableListOf())

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.BackDropScaffold -> {
                    if (backdropState.isRevealed)
                        backdropState.conceal()
                    else
                        backdropState.reveal()
                }
                else -> Unit
            }
        }
    }

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    var searchBarValue by remember {
        mutableStateOf("")
    }

    val upcomingReminders = state.filter { reminder -> !reminder.isDone && !reminder.isPinned }
    val pinnedReminders = state.filter { reminder -> reminder.isPinned && !reminder.isDone }
    val completedReminders = state.filter { reminder -> reminder.isDone }
    val filteredReminders =
        state.filter { reminder ->
            (reminder.isDone && selectedCategory == "Completed") || reminder.categories!!.contains(
                selectedCategory)
        }
    val searchResults = state.filter { reminder ->
        if (searchBarValue.isNotEmpty()) reminder.title.contains(searchBarValue) else false
    }

    val scope = rememberCoroutineScope()

    val interactionSource = MutableInteractionSource()

    val isFocused by interactionSource.collectIsFocusedAsState()

    val searchIconColor by animateColorAsState(
        targetValue = if (isFocused && searchBarValue.isNotEmpty()) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(
            alpha = 0.5f
        ), animationSpec = tween(300)
    )

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {
            Crossfade(targetState = backdropState.isConcealed) { isConcealed ->
                if (isConcealed)
                    TopBar(
                        title = "Reminders",
                        onEvent = viewModel::onEvent,
                        isFilterOpened = viewModel.isFilterOpened
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
                    )
            }
        },
        backLayerContent = {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 15.dp, horizontal = 15.dp),
            ) {
                item {
                    Crossfade(targetState = searchResults.isNotEmpty()) {
                        if (it)
                            ReminderCategory(
                                categoryName = "Search results",
                                reminders = searchResults,
                                onEvent = viewModel::onEvent
                            )
                    }
                }
            }
        },
        frontLayerContent = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(RemindersListEvents.OnAddClick)
                        }
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                    }
                }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 15.dp, horizontal = 15.dp),
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .animateContentSize()
                                .then(if (viewModel.isFilterOpened) Modifier else Modifier.height(0.dp))
                        ) {

                            Text(text = "Filters")

                            FlowRow(
                                mainAxisSpacing = 10.dp
                            ) {
                                TextAndRadioButton(
                                    title = "All",
                                    selected = selectedCategory == "All",
                                    onClick = { selectedCategory = it }
                                )

                                TextAndRadioButton(
                                    title = "Completed",
                                    selected = selectedCategory == "Completed",
                                    onClick = { selectedCategory = it }
                                )

                                categories?.let { categories ->
                                    repeat(categories.size) { index ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable {
                                                selectedCategory = categories[index]
                                            }
                                        ) {
                                            RadioButton(
                                                selected = selectedCategory == categories[index],
                                                onClick = { selectedCategory = categories[index] }
                                            )
                                            Text(
                                                text = categories[index],
                                                style = MaterialTheme.typography.body2
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    }

                    item {
                        if (pinnedReminders.isNotEmpty() && selectedCategory == "All") {
                            ReminderCategory(
                                categoryName = "Pinned",
                                reminders = pinnedReminders,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }

                    item {

                        if (upcomingReminders.isNotEmpty() && selectedCategory == "All") {
                            Spacer(modifier = Modifier.height(10.dp))
                            ReminderCategory(
                                categoryName = "Upcoming",
                                reminders = upcomingReminders,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }
                    item {
                        if (completedReminders.isNotEmpty() && selectedCategory == "All") {
                            Spacer(modifier = Modifier.height(10.dp))
                            ReminderCategory(
                                categoryName = "Completed",
                                reminders = completedReminders,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }

                    item {
                        if (filteredReminders.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ReminderCategory(
                                categoryName = "Filtered",
                                reminders = filteredReminders,
                                onEvent = viewModel::onEvent
                            )
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
private fun TextAndRadioButton(
    title: String,
    onClick: (String) -> Unit,
    selected: Boolean,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onClick(title)
        }
    ) {
        RadioButton(
            selected = selected,
            onClick = { onClick(title) }
        )
        Text(
            text = title,
            style = MaterialTheme.typography.body2
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun ReminderCategory(
    categoryName: String,
    reminders: List<ReminderEntity>,
    onEvent: (RemindersListEvents) -> Unit,
) {
    Column {
        Text(text = categoryName)

        Spacer(modifier = Modifier.height(10.dp))

        StaggeredVerticalGrid {
            repeat(reminders.size) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                ) {

                    ReminderCard(
                        reminder = ReminderEntity(
                            title = reminders[it].title,
                            description = reminders[it].description,
                            isDone = reminders[it].isDone,
                            isPinned = reminders[it].isPinned,
                            categories = reminders[it].categories,
                            color = reminders[it].color,
                            repeat = reminders[it].repeat,
                            date = reminders[it].date,
                            id = reminders[it].id
                        ),
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onEvent: (RemindersListEvents) -> Unit,
    isFilterOpened: Boolean,
    title: String,
) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(
                onClick = {
                    onEvent(RemindersListEvents.OnSearchClick)
                }
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null)
            }

            IconButton(
                onClick = {
                    onEvent(RemindersListEvents.OnFilterClick)
                }
            ) {
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

            IconButton(
                onClick = {
                    onEvent(RemindersListEvents.OnSettingsClick)
                }
            ) {
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
                                        text = "Search reminder",
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
                                                .clip(CircleShape)
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
                modifier = Modifier.rotate(270f)
            ) {
                Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    )
}