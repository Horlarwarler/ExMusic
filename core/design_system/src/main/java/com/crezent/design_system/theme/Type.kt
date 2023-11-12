package com.crezent.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.crezent.design_system.R

// Set of Material typography styles to start with

//val gilroyFamily = FontFamily(
//    Font(R.font.gilroy_bold, weight = FontWeight.Bold),
//    Font(R.font.gilroy_medium, weight = FontWeight.Medium),
//    Font(R.font.gilroy_regular, weight = FontWeight.Normal),
//    Font(R.font.gilroy_heavy, weight = FontWeight(800)),
//    Font(R.font.gilroy_light, weight = FontWeight.Light),
//
//
//)
//
//
//val groteskFamily = FontFamily(
//    Font(R.font.grotesk_black, weight = FontWeight.Black),
//    Font(R.font.grotesk_bold, weight = FontWeight.Bold),
//    Font(R.font.grotesk_light, weight = FontWeight.Light),
//    Font(R.font.grotesk_roman, ),
//
//    )
val poppings = FontFamily(
    Font(R.font.poppins_bold, weight = FontWeight(700)),
    Font(R.font.poppins_regular, weight = FontWeight(400)),
    Font(R.font.poppins_semi_bold, weight = FontWeight(600)),
    Font(R.font.poppins_medium, weight = FontWeight(500)),
)
val Typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(700),
        fontSize = 25.sp,
        lineHeight = 35.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 16.sp,
        lineHeight = 25.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(700),
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(600),
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = poppings,
        fontWeight = FontWeight(400),
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.sp,
    )
)

