package com.atitienei_daniel.reeme.presentation.ui.screens.on_boarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.atitienei_daniel.reeme.R
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue700
import com.atitienei_daniel.reeme.presentation.theme.DarkBlue800

@Composable
fun OnBoardingScreen() {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .componentRegistry {
            add(SvgDecoder(context))
        }
        .build()

    /*
    * TODO Crossfade animation to all widgets
    * */

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 30.dp)
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.weight(4f), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberImagePainter(
                    data = R.drawable.ic_reminder,
                    builder = { crossfade(true) },
                    imageLoader = imageLoader
                ), contentDescription = null
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Create your reminders",
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Create your own reminders at a low price cost.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(text = "Register")
                }

                Spacer(modifier = Modifier.width(15.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 10.dp)
                ) {
                    Text(text = "Login")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = R.drawable.ic_google,
                        builder = {
                            crossfade(true)
                        },
                        imageLoader = imageLoader
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(text = "Continue with Google", color = DarkBlue800)
            }
        }
    }
}