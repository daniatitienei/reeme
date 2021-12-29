package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import androidx.compose.animation.Crossfade
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
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atitienei_daniel.reeme.R
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue800
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.components.StaggeredVerticalGrid
import com.atitienei_daniel.reeme.presentation.ui.utils.Screens
import com.atitienei_daniel.reeme.presentation.utils.intToColor
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun RemindersScreen(
    navController: NavController,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val scrollState = rememberScrollState()

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )

    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()

    var isDialogOpened by remember {
        mutableStateOf(false)
    }

    var isFilterOpened by remember {
        mutableStateOf(false)
    }

    var newCategoryTitle by remember {
        mutableStateOf("")
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

    if (isDialogOpened)
        CreateCategoryAlertDialog(
            categoryTitleValue = newCategoryTitle,
            onValueChange = { newCategoryTitle = it },
            onDismissRequest = { isDialogOpened = false },
            onCreateCategoryButtonClick = { /*TODO*/ },
            onDone = {
                keyboardController?.hide()
            }
        )

    BackdropScaffold(
        scaffoldState = backdropState,
        appBar = {
            val rotationAngle by animateFloatAsState(targetValue = if (backdropState.isRevealed) 90f else 0f)

            Crossfade(targetState = backdropState.isConcealed) { isConcealed ->
                if (isConcealed)
                    TopBar(
                        title = "Reminders",
                        angle = rotationAngle,
                        onMenuIconClick = { /*TODO*/ },
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
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
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
                ) {
                    Column(
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (isFilterOpened) {
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

                    /*TODO Make it better*/
                    if (state.isNotEmpty()) {
                        Text(text = "Upcoming")

                        Spacer(modifier = Modifier.height(10.dp))

                        StaggeredVerticalGrid {
                            repeat(state.size) {
                                Box(
                                    modifier = Modifier.padding(
                                        start = if (it % 2 != 0) 5.dp else 0.dp,
                                        end = if (it % 2 != 0) 0.dp else 5.dp,
                                        bottom = 10.dp
                                    )
                                ) {
                                    ReminderCard(reminder = state[it])
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

                /* TODO Add done imeAction */
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
    onMenuIconClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onFilterIconClick: () -> Unit,
    isFilterOpened: Boolean,
    angle: Float,
    title: String
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onMenuIconClick) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = null,
                    modifier = Modifier.rotate(angle)
                )
            }
        },
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
    reminder: Reminder
) {
    Card(
        backgroundColor = intToColor(reminder.color),
        onClick = { /*TODO*/ },
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

            Text(
                text = reminder.description ?: "",
                style = MaterialTheme.typography.body2,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .padding(5.dp)
            ) {
                Text(
                    text = reminder.timestamp.toDate().toString(),
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun RemindersPreview() {
    ReemeTheme {
        RemindersScreen(rememberNavController())
    }
}