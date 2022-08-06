package com.example.talimlectures.Destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.talimlectures.presentation.splashScreen.SplashComposable
import com.example.talimlectures.util.Screens
import com.example.talimlectures.viewModels.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable

// Declaring a  navigation settings that contains our Composable
fun NavigationSetup(
    sharedViewModel: SharedViewModel,
    navController: NavHostController
){

    // Declaring the current screen of our app
    // Using remember to save the state of the screen
    val currentScreen = remember{
        Screens(navController = navController)
    }

    // Using AnimatedNavHost to handle animation during transition of screen
    AnimatedNavHost(
        navController = navController,
        // Where the app will start
        startDestination = "splash"
    ){

        // This contains our composable

       //Splash Screen composable
     SplashComposable(
         // This will call the navigateFromSplash lambda in screen
         navigateToLecture = currentScreen.navigateFromSplash,
         sharedViewModel =  sharedViewModel
     )
        //Home screen composable
     HomeComposable(
         navigateToLecture = currentScreen.navigateToLectures,
         navController,
         navigateToPlayer = currentScreen.navigateToPlayer
     )
        //Lecture screen composable
     LectureComposable(
        navigateToHome = currentScreen.navigateToHome,
        navController = navController,
         navigateToPlayer = currentScreen.navigateToPlayer

     )
        // Player Screen Composable
     PlayerComposable(
         navController = navController
     )
 }

}