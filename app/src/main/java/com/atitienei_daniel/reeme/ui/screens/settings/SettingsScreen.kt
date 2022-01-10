package com.atitienei_daniel.reeme.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.atitienei_daniel.reeme.ui.theme.Red900
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import kotlinx.coroutines.flow.collect

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
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

    var showFeedbackAlertDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val privacyPolicyIntent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://github.com/daniatitienei/reeme-privacy-policy/blob/main/privacy-policy.md")
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.PopBackStack -> {
                    onPopBackStack(event)
                }
                is UiEvent.AlertDialog -> {
                    showFeedbackAlertDialog = event.isOpen
                }
                else -> Unit
            }
        }
    }

    if (showFeedbackAlertDialog)
        Feedback(
            onEvent = viewModel::onEvent
        )

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
                        text = {
                            Text(
                                text = when (currentTheme) {
                                    Theme.AUTO -> "Auto"
                                    Theme.LIGHT -> "Light"
                                    else -> "Dark"
                                },
                            )
                        },
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
                onClick = { viewModel.onEvent(SettingsEvents.ToggleFeedbackAlert(isOpen = true)) }
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
                onClick = {
                    context.startActivity(privacyPolicyIntent)
                }
            ) {
                ListItem(
                    text = { Text(text = "Privacy policy") },
                    icon = {
                        Icon(
                            Icons.Outlined.Policy,
                            contentDescription = "Privacy policy",
                            tint = MaterialTheme.colors.primary
                        )
                    }
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun Feedback(
    onEvent: (SettingsEvents) -> Unit,
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("animations/feedback.json")
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    var feedbackTextValue by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = { onEvent(SettingsEvents.ToggleFeedbackAlert(isOpen = false)) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.padding(40.dp),
        buttons = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(composition = composition,
                    progress = progress,
                    modifier = Modifier.size(200.dp))

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = feedbackTextValue,
                    onValueChange = { feedbackTextValue = it },
                    placeholder = {
                        Text(
                            text = "Suggestions, bugs, etc...",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.typography.body2.color.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    TextButton(
                        onClick = {
                            onEvent(SettingsEvents.ToggleFeedbackAlert(isOpen = false))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel", color = Red900)
                    }

                    TextButton(
                        onClick = { onEvent(SettingsEvents.SendFeedback(text = feedbackTextValue)) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Save", color = MaterialTheme.colors.primary)
                    }
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun ListItemWithRadioButton(
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