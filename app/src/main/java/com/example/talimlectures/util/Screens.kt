package com.example.talimlectures.util

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.talimlectures.util.Constant.HOME_SCREEN_NAME
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_NAME
import com.example.talimlectures.util.Constant.SPLASH_SCREEN_NAME

class Screens(navController: NavHostController) {
    // Declaring  a lambda to handle navigation from Splash
    val navigateFromSplash:() -> Unit = {
        // Navigating to Home Screen
        navController.navigate(HOME_SCREEN_NAME){
            // Pop the backstack to Splash screen then  remove it also from backstack
            //This prevents it from going to Splash Screen after pressing back
            popUpTo(SPLASH_SCREEN_NAME){
                inclusive = true
            }
        }
    }

    // Declaring a lambda to Navigating Home Screen
    val navigateToHome:()->Unit = {
        navController.navigate(HOME_SCREEN_NAME){
            // When we navigate from lecture screen, to home screen,
            // There exist lecture screen, home screen in the back stack
            //We need to remove the Lecture screen from the back stack
            popUpTo(LECTURE_SCREEN_NAME){
                inclusive = true
            }

        }
    }
    //Declaring a lambda to navigate to Lecture Screen
    val navigateToLectures:()->Unit = {
        navController.navigate(LECTURE_SCREEN_NAME){
            //Popping the backstack  to Home Screen
            //We should not remove
            popUpTo(HOME_SCREEN_NAME){
                //This should be removed
                inclusive = true
            }

        }
    }

    // Navigating to player Screen
    // previousScreenName argument  so as to navigate to it back
    val navigateToPlayer:(previousScreenName:String)->Unit = {
        previousScreenName->
        // Navigate to the specified Screen name
        navController.navigate(route = "player/${previousScreenName}"){
            // Popping up to lecture PLAYER_SCREEN_NAME then remove it from  backstack
            popUpTo(PLAYER_SCREEN_NAME){
                    inclusive = true
            }
        }
    }
}