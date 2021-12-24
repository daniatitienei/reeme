package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue700
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue800
import com.atitienei_daniel.reeme.presentation.theme.Purple700
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.screens.reminders.components.StaggeredVerticalGrid
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RemindersScreen() {

    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val checkedList = remember {
        mutableStateListOf<Int>()
    }

    var isDialogOpened by remember {
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
        AlertDialog(
            onDismissRequest = { isDialogOpened = false },
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
                        value = newCategoryTitle,
                        onValueChange = { newCategoryTitle = it },
                        placeholder = { Text(text = "Enter title") }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Text(text = "Create")
                    }
                }
            }
        )

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetContent = {
            Box {
                LazyColumn {
                    item {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 30.dp)
                                .padding(top = 20.dp, bottom = 10.dp)
                        ) {
                            Text(text = "Categories", style = MaterialTheme.typography.h6)
                        }
                    }
                    items(8) { index ->
                        var isChecked by remember {
                            mutableStateOf(false)
                        }

                        Box(modifier = Modifier.clickable {
                            isChecked = !isChecked

                            if (isChecked)
                                checkedList.add(index)
                            else
                                checkedList.remove(index)
                        }) {
                            ListItem(
                                icon = {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            isChecked = it

                                            if (isChecked)
                                                checkedList.add(index)
                                            else
                                                checkedList.remove(index)
                                        },
                                        colors = CheckboxDefaults.colors(uncheckedColor = DarkBlue800)
                                    )
                                },
                                text = {
                                    Text(text = "Work")
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.padding(bottom = 55.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                                .padding(bottom = 15.dp)
                        ) {
                            Button(
                                onClick = { isDialogOpened = true },
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 10.dp)
                            ) {
                                Text(
                                    text = if (checkedList.isEmpty()) "Create new category" else "Continue",
                                )
                            }
                        }
                    }
                }
            }
        }
    ) {
        BackdropScaffold(
            scaffoldState = backdropState,
            appBar = {
                Crossfade(targetState = backdropState.isConcealed) { isConcealed ->
                    if (isConcealed)
                        TopAppBar(
                            title = { Text(text = "Reminders") },
                            navigationIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Rounded.Menu, contentDescription = null)
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    scope.launch {
                                        backdropState.reveal()
                                    }
                                }) {
                                    Icon(Icons.Rounded.Search, contentDescription = null)
                                }

                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(Icons.Rounded.FilterList, contentDescription = null)
                                }
                            },
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MaterialTheme.colors.primary
                        )
                    else
                        TopAppBar(
                            title = {
                                BasicTextField(
                                    value = searchBarValue,
                                    onValueChange = { searchBarValue = it },
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
                                                .fillMaxWidth(0.9f)
                                                .background(Color.Black.copy(alpha = 0.07f))
                                                .padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Rounded.Search,
                                                contentDescription = null,
                                                tint = searchIconColor
                                            )

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Box {
                                                Crossfade(targetState = searchBarValue.isEmpty()) { isEmpty ->
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
                                            }
                                        }
                                    }
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        backdropState.conceal()
                                    }
                                }) {
                                    Icon(Icons.Rounded.Close, contentDescription = null)
                                }
                            },
                            backgroundColor = MaterialTheme.colors.background,
                            contentColor = MaterialTheme.colors.primary
                        )
                }
            },
            backLayerContent = {
                Column(modifier = Modifier.fillMaxSize()) {

                }
            },
            headerHeight = 0.dp,
            stickyFrontLayer = false,
            gesturesEnabled = false,
            backLayerBackgroundColor = MaterialTheme.colors.background,
            frontLayerContent = {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 15.dp, vertical = 15.dp)
                ) {
                    item {
                        Text(text = "Upcoming")

                        Spacer(modifier = Modifier.height(10.dp))

                        StaggeredVerticalGrid {
                            repeat(18) {
                                Box(
                                    modifier = Modifier.padding(
                                        start = if (it % 2 != 0) 5.dp else 0.dp,
                                        end = if (it % 2 != 0) 0.dp else 5.dp,
                                        bottom = 10.dp
                                    )
                                ) {
                                    if (it % 3 != 0)
                                        ReminderCard(
                                            color = Color(0xffD2F49B),
                                            title = "Take a pill",
                                            description = "Lorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsumLorem ipsum lorem ipsum lorem ipsum",
                                            time = "Tommorow, 4:20"
                                        )
                                    else
                                        ReminderCard(
                                            color = Color(0xffF49B9B),
                                            title = "Take a pill",
                                            description = "Lorem ipsum lorem ipsum lorem ipsum",
                                            time = "Tommorow, 4:20"
                                        )
                                }
                            }
                        }
                    }
                }
            }
        ) {
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ReminderCard(
    color: Color,
    title: String,
    description: String,
    time: String
) {
    Card(
        backgroundColor = color,
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light
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
                    text = time,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ReminderCardPreview() {
    ReemeTheme {
        ReminderCard(
            color = Color(0xffF49B9B),
            title = "Take a pill",
            description = "Lorem ipsum lorem ipsum lorem ipsum",
            time = "Tommorow, 4:20"
        )
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun RemindersPreview() {
    ReemeTheme {
        RemindersScreen()
    }
}