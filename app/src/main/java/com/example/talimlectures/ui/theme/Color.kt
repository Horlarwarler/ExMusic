package com.example.talimlectures.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

//val NightBackground = Color(0xFF181818)
//val NightSearchBackground = Color(0xFF121212)
//val NightLectureBackground = Color(0xFF212121)
//val NightIconColor = Color(0xFFFFFFFF)
//val NightTitleColor = Color(0xFFF3F3F3)
//val NightDescriptionColor = Color(0xFFAAAAAA)

//val Colors.BackgroundColor:Color
//@Composable
//get() = if(isSystemInDarkTheme()) NightBackground else LightBackground
//val Colors.LectureBackgroundColor:Color
//    @Composable
//    get() = if(isSystemInDarkTheme()) NightLectureBackground else LightLectureBackground
//val Colors.SearchBackgroundColor:Color
//@Composable
//get() = if(isSystemInDarkTheme()) NightSearchBackground else LightSearchBackground
//val Colors.IconColor:Color
//@Composable
//get() = if(isSystemInDarkTheme()) NightIconColor else LightIconColor
//val Colors.TitleColor:Color
//@Composable
//get() = if(isSystemInDarkTheme()) NightTitleColor else LightTitleColor
//val Colors.DescriptionColor:Color
//@Composable
//get() = if(isSystemInDarkTheme()) NightDescriptionColor else LightDescriptionColor

val BackgroundColor = Color(0xFF181B2C)

val SearchBackgroundColor = Color(0xFF292E4B)
val SearchTextColor =  Color(0x33FFFFFF)
val IconColor = Color(0xFFFFFFFF)
val BottomNavigationIconColor = Color(0xFF63666E)
val BottomNavigationTextColor = Color(0x47FFFFFF)
val SelectedBottomNavigationTextColor = Color(0xFFD9519D)
private  val gradientColor = Brush.linearGradient(
    colors = listOf(
        Color(0xFFED8770), Color(0xFFD9519D)
    )
)
val PlayIconBorderColor = gradientColor
val SelectedNavigationIconColor = gradientColor
val TitleColor = Color(0x99FFFFFF)
val FavouriteColor = Color(0xFFEE5A30)
val DescriptionColor = Color(0x47FFFFFF)
val TextColor = Color(0xCCFFFFFF)
val SelectedCategoryColor = Color(0xCCD9519D)
val Light = Color(0xFFD0CCE2)
val MiniPlayerIconColor = Color(0xFFE2E2E2)
val MiniPlayerBackgroundColor = Color(0xFF0F1222)
val MiniPlayerCloseIcon =Color(0xCCFFFFFF)
val MiniPlayerTitleColor = Color(0xFFFFFFFF)
val MiniPlayerDescriptionColor = Color(0xB3FFFFFF)

