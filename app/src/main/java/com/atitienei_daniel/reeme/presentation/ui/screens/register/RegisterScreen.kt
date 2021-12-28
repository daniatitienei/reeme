package com.atitienei_daniel.reeme.presentation.ui.screens.register

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atitienei_daniel.reeme.presentation.theme.Red900
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.utils.Screens

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun RegisterScreen(
    navController: NavController = rememberNavController(),
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var confirmPasswordError by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hello!", style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Welcome! We’re glad that you are here!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(20.dp))

        CredentialTextField(
            value = email,
            onValueChange = {
                if (!viewModel.emailError.value.isNullOrEmpty())
                    viewModel.clearEmailError()
                email = it
            },
            placeholderText = "Email",
            textFieldType = TextFieldType.EMAIL,
            focusManager = focusManager,
            errorMessage = viewModel.emailError.value
        )

        Spacer(modifier = Modifier.height(10.dp))

        CredentialTextField(
            value = name,
            onValueChange = {
                if (!viewModel.nameError.value.isNullOrEmpty())
                    viewModel.clearNameError()
                name = it
            },
            placeholderText = "Name",
            textFieldType = TextFieldType.NAME,
            focusManager = focusManager,
            errorMessage = viewModel.nameError.value
        )

        Spacer(modifier = Modifier.height(10.dp))

        CredentialTextField(
            value = password,
            onValueChange = {
                if (!viewModel.passwordError.value.isNullOrEmpty())
                    viewModel.clearPasswordError()
                password = it
            },
            placeholderText = "Password",
            textFieldType = TextFieldType.PASSWORD,
            focusManager = focusManager,
            errorMessage = viewModel.passwordError.value
        )

        Spacer(modifier = Modifier.height(10.dp))

        CredentialTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPasswordError = if (password != it)
                    "Passwords must match."
                else
                    null

                confirmPassword = it
            },
            placeholderText = "Confirm password",
            textFieldType = TextFieldType.CONFIRM_PASSWORD,
            focusManager = focusManager,
            keyboardController = keyboardController,
            errorMessage = confirmPasswordError
        )

        Spacer(modifier = Modifier.height(25.dp))

        Button(
            onClick = {
                if (password == confirmPassword)
                    viewModel.registerUser(
                        email = email,
                        name = name,
                        password = password,
                        onAccountCreated = {
                            navController.navigate(Screens.Reminders.route) {
                                launchSingleTop = true

                                popUpTo(Screens.OnBoardingScreen.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            navController.navigate(Screens.Login.route) {
                launchSingleTop = true

                popUpTo(Screens.Register.route) {
                    inclusive = true
                }
            }
        }) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary.copy(0.7f))) {
                    append("Already a member? ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("Login now")
                }
            })
        }
    }
}

private enum class TextFieldType {
    EMAIL,
    NAME,
    PASSWORD,
    CONFIRM_PASSWORD
}

@ExperimentalComposeUiApi
@Composable
private fun CredentialTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    textFieldType: TextFieldType,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    errorMessage: String? = null
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.animateContentSize()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholderText,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f)
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = MaterialTheme.colors.primary.copy(0.3f),
            ),
            singleLine = true,
            visualTransformation =
            if ((textFieldType == TextFieldType.PASSWORD || textFieldType == TextFieldType.CONFIRM_PASSWORD) && !isPasswordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (textFieldType == TextFieldType.PASSWORD || textFieldType == TextFieldType.CONFIRM_PASSWORD) {
                    Crossfade(targetState = isPasswordVisible) { isVisible ->
                        if (isVisible)
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(Icons.Rounded.VisibilityOff, contentDescription = null)
                            }
                        else
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(Icons.Rounded.Visibility, contentDescription = null)
                            }
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = when (textFieldType) {
                    TextFieldType.CONFIRM_PASSWORD -> ImeAction.Done
                    else -> ImeAction.Next
                },
                keyboardType = when (textFieldType) {
                    TextFieldType.EMAIL -> KeyboardType.Email
                    TextFieldType.PASSWORD -> KeyboardType.Password
                    TextFieldType.CONFIRM_PASSWORD -> KeyboardType.Password
                    else -> KeyboardType.Text
                },
                capitalization = if (textFieldType == TextFieldType.NAME) KeyboardCapitalization.Words else KeyboardCapitalization.None
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                },

                onDone = {
                    keyboardController?.hide()
                }
            ),
            isError = !errorMessage.isNullOrEmpty()
        )
        if (!errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorMessage,
                fontSize = MaterialTheme.typography.caption.fontSize,
                color = Red900
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    ReemeTheme {
        RegisterScreen()
    }
}
