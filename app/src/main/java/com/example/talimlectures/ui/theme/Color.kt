package com.example.talimlectures.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val NightBackground = Color(0xFF181818)
val NightSearchBackground = Color(0xFF121212)
val NightLectureBackground = Color(0xFF212121)
val NightIconColor = Color(0xFFFFFFFF)
val NightTitleColor = Color(0xFFF3F3F3)
val NightDescriptionColor = Color(0xFFAAAAAA)

val LightBackground = Color(0xFF2B28D5)
val LightSearchBackground = Color(0xFF5441B8)
val LightLectureBackground = Color(0xFF5441B8)

val LightIconColor = Color(0xFFFFFFFF)
val LightTitleColor = Color(0xFFFFFFFF)
val LightDescriptionColor = Color(0xFFD0CCE2)

val Colors.BackgroundColor:Color
@Composable
get() = if(isSystemInDarkTheme()) NightBackground else LightBackground
val Colors.LectureBackgroundColor:Color
    @Composable
    get() = if(isSystemInDarkTheme()) NightLectureBackground else LightLectureBackground
val Colors.SearchBackgroundColor:Color
@Composable
get() = if(isSystemInDarkTheme()) NightSearchBackground else LightSearchBackground
val Colors.IconColor:Color
@Composable
get() = if(isSystemInDarkTheme()) NightIconColor else LightIconColor
val Colors.TitleColor:Color
@Composable
get() = if(isSystemInDarkTheme()) NightTitleColor else LightTitleColor
val Colors.DescriptionColor:Color
@Composable
get() = if(isSystemInDarkTheme()) NightDescriptionColor else LightDescriptionColor

