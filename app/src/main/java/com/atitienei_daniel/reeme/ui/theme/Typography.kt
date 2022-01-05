package com.atitienei_daniel.reeme.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.atitienei_daniel.reeme.R

private val Gilroy = FontFamily(
    Font(R.font.gilroy_extra_bold),
    Font(R.font.gilroy_light, weight = FontWeight.Light),
)

fun ReemeTypography(isDarkTheme: Boolean): Typography {
    val color = if (!isDarkTheme) DarkBlue800 else DarkBlue500

    return Typography(
        h3 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W600,
            fontSize = 34.sp,
            color = color
        ),
        h4 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W600,
            fontSize = 30.sp,
            color = color
        ),
        h5 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W600,
            fontSize = 24.sp,
            color = color
        ),
        h6 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W600,
            fontSize = 20.sp,
            color = color
        ),
        subtitle1 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            color = color
        ),
        subtitle2 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            color = color
        ),
        body1 = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = color
        ),
        body2 = TextStyle(
            fontFamily = Gilroy,
            fontSize = 14.sp,
            color = color
        ),
        button = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            color = Color.White
        ),
        caption = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = color
        ),
        overline = TextStyle(
            fontFamily = Gilroy,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = color
        )
    )
}
