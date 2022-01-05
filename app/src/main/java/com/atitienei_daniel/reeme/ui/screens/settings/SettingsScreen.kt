package com.atitienei_daniel.reeme.ui.screens.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.atitienei_daniel.reeme.ui.theme.ReemeTheme
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collect

@ExperimentalMaterialApi
@Composable
fun SettingsScreen(
    onPopBackStack: (UiEvent) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {

    var themeIsExpanded by remember {
        mutableStateOf(false)
    }

    val arrowRotationAnimation by animateFloatAsState(targetValue = if (themeIsExpanded) 180f else 0f)

    val currentTheme by viewModel.theme.collectAsState(initial = Theme.AUTO)

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(SettingsEvents.OnNavigationIconClick)
                        }
                    ) {
                        Icon(
                            Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "back",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp)
        ) {
            Text(text = "Theme")

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.animateContentSize(
                    animationSpec = tween(
                        easing = LinearOutSlowInEasing
                    )
                ),
                onClick = {
                    themeIsExpanded = !themeIsExpanded
                }
            ) {
                Column {
                    ListItem(
                        text = { Text(text = "Auto") },
                        icon = {
                            Icon(
                                when (currentTheme) {
                                    Theme.AUTO -> Icons.Outlined.SettingsSuggest
                                    Theme.LIGHT -> Icons.Outlined.DarkMode
                                    else -> Icons.Rounded.DarkMode
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary
                            )
                        },
                        trailing = {
                            Icon(
                                Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.rotate(arrowRotationAnimation)
                            )
                        }
                    )

                    if (themeIsExpanded) {
                        ListItemWithRadioButton(
                            text = "Auto",
                            selected = currentTheme == Theme.AUTO,
                            onClick = {
                                viewModel.onEvent(SettingsEvents.OnChangeThemeClick(Theme.AUTO))
                            },
                            icon = Icons.Outlined.SettingsSuggest
                        )
                        ListItemWithRadioButton(
                            text = "Light",
                            selected = currentTheme == Theme.LIGHT,
                            onClick = {
                                viewModel.onEvent(SettingsEvents.OnChangeThemeClick(Theme.LIGHT))
                            },
                            icon = Icons.Outlined.DarkMode
                        )
                        ListItemWithRadioButton(
                            text = "Dark",
                            selected = currentTheme == Theme.DARK,
                            onClick = {
                                viewModel.onEvent(SettingsEvents.OnChangeThemeClick(Theme.DARK))
                            },
                            icon = Icons.Rounded.DarkMode
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "General")

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                onClick = { /*TODO*/ }
            ) {
                ListItem(
                    text = { Text(text = "Feedback") },
                    icon = {
                        Icon(
                            Icons.Outlined.Feedback,
                            contentDescription = "Feedback",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            Card(
                onClick = { /*TODO*/ }
            ) {
                ListItem(
                    text = { Text(text = "Privacy policy") },
                    icon = {
                        Icon(
                            Icons.Outlined.Policy,
                            contentDescription = "Feedback",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ListItemWithRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
) {
    ListItem(
        text = { Text(text = text) },
        icon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colors.primary) },
        trailing = {
            RadioButton(selected = selected, onClick = onClick)
        },
        modifier = Modifier
            .padding(start = 10.dp)
            .clickable {
                onClick()
            }
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    ReemeTheme {
        SettingsScreen({})
    }
}