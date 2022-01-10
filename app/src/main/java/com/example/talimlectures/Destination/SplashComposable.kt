package com.example.talimlectures.Destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.talimlectures.Screens.SplashScreens.SplashScreen

import com.example.talimlectures.util.Constant.SPLASH_SCREEN_NAME

fun NavGraphBuilder.SplashComposable(
    navigateToLecture:() -> Unit
){
    composable(
        route = SPLASH_SCREEN_NAME,
    ){
        SplashScreen (navigateToLecture = navigateToLecture)
    }
}