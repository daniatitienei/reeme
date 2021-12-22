package com.atitienei_daniel.reeme.presentation.ui.screens.login

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
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
import androidx.navigation.NavController
import com.atitienei_daniel.reeme.presentation.theme.ReemeTheme
import com.atitienei_daniel.reeme.presentation.ui.utils.Screens
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun LoginScreen(
    navController: NavController = rememberAnimatedNavController()
) {

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Hello Again!",
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Welcome back you’ve been missed!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.height(20.dp))

        CredentialTextField(
            value = email,
            onValueChange = { email = it },
            placeholderText = "Email",
            textFieldType = TextFieldType.EMAIL,
            focusManager = focusManager
        )

        Spacer(modifier = Modifier.height(10.dp))

        CredentialTextField(
            value = password,
            onValueChange = { password = it },
            placeholderText = "Password",
            textFieldType = TextFieldType.PASSWORD,
            focusManager = focusManager,
            keyboardController = keyboardController
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .padding(top = 5.dp)
        ) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "Recover password",
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 15.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = {
            navController.navigate(Screens.Register.route) {
                launchSingleTop = true

                popUpTo(Screens.Login.route) {
                    inclusive = true
                }
            }
        }) {
            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary.copy(0.7f))) {
                    append("Not a member? ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("Register now")
                }
            })
        }
    }
}

private enum class TextFieldType {
    EMAIL,
    PASSWORD,
}

@ExperimentalComposeUiApi
@Composable
private fun CredentialTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    textFieldType: TextFieldType,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current
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
        visualTransformation = if (textFieldType == TextFieldType.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = when (textFieldType) {
                TextFieldType.PASSWORD -> ImeAction.Done
                else -> ImeAction.Next
            },
            keyboardType = when (textFieldType) {
                TextFieldType.EMAIL -> KeyboardType.Email
                TextFieldType.PASSWORD -> KeyboardType.Password
            },
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            },

            onDone = {
                keyboardController?.hide()
            }
        )
    )
}

@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    ReemeTheme {
        LoginScreen()
    }
}