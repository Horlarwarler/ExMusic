package com.example.talimlectures.Destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.talimlectures.util.Screens


@Composable
fun NavigationSetup(
    navController: NavHostController
){
    val currentScreen = remember(navController) {
        Screens(navController = navController)
    }
 NavHost(navController = navController, startDestination = "splash" ){

     LectureComposable (currentScreen.navigateToMusic)
     PlayerComposable (currentScreen.navigateToLectures)
     SplashComposable (currentScreen.navigateFromSplash)
 }


}