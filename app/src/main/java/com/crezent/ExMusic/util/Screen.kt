package com.crezent.ExMusic.util

import android.util.Log
import androidx.navigation.NavHostController
import com.crezent.common.util.Constant.ADMIN_HOME_SCREEN
import com.crezent.common.util.Constant.HOME_SCREEN_NAME
import com.crezent.common.util.Constant.LOGIN_SCREEN
import com.crezent.common.util.Constant.MUSIC_SCREEN_NAME
import com.crezent.common.util.Constant.ONBOARD_SCREEN
import com.crezent.common.util.Constant.ONBOARD_SECOND_SCREEN
import com.crezent.common.util.Constant.ONBOARD_THIRD_SCREEN
import com.crezent.common.util.Constant.PROFILE_UPDATE_SCREEN
import com.crezent.common.util.Constant.SIGN_UP_SCREEN
import com.crezent.common.util.Constant.SPLASH_SCREEN
import com.crezent.common.util.Constant.UPLOAD_DASHBOARD_SCREEN
import com.crezent.common.util.Constant.UPLOAD_FILE_SCREEN
import com.crezent.common.util.Constant.USER_REGISTER_SCREEN


class Screen(navController: NavHostController) {
    // Declaring  a lambda to handle navigation from Splash
    val navigateFromSplash:() -> Unit = {
        navController.navigate(HOME_SCREEN_NAME){
            popUpTo(SPLASH_SCREEN){
                inclusive = true
            }
        }
    }

    val navigateToAdminHome : () -> Unit  = {
        Log.d("AdminHome","Navigate to Admin Home")

        navController.navigate(ADMIN_HOME_SCREEN)
        {
            popUpTo(HOME_SCREEN_NAME)
        }
    }

    val navigateToHomeFromAdmin : () -> Unit  = {
        navController.navigate(HOME_SCREEN_NAME){
            popUpTo(ADMIN_HOME_SCREEN){
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
            popUpTo(HOME_SCREEN_NAME){
                inclusive = true
            }

        }
    }
    //Declaring a lambda to navigate to Lecture Screen

    // Navigating to player Screen
    // previousScreenName argument  so as to navigate to it back
//    val navigateToPlayer:(previousScreenName:String)->Unit = {
//        previousScreenName->
//        // Navigate to the specified Screen name
//        navController.navigate(route = "player/${previousScreenName}"){
//            // Popping up to lecture PLAYER_SCREEN_NAME then remove it from  backstack
//            popUpTo(PLAYER_SCREEN_NAME){
//                    inclusive = true
//            }
//        }
//    }

    val navigateToUserScreen: () -> Unit = {
        navController.navigate(route = USER_REGISTER_SCREEN){
            popUpTo(ADMIN_HOME_SCREEN){
                inclusive = true
            }
        }
    }

    val navigateToOnBoard : () -> Unit =  {
        navController.navigate(ONBOARD_SCREEN){
            popUpTo(SPLASH_SCREEN){
                inclusive = true
            }
        }
    }
    val navigateToLoginFromOnBoard : () -> Unit = {
        navController.navigate(LOGIN_SCREEN){
            popUpTo(ONBOARD_SCREEN){
                inclusive = true
            }
        }
    }

    val navigateToLogin : (popUpTo:String, inclusive:Boolean) -> Unit = {
        popUpTo: String, isInclusive: Boolean ->

        navController.navigate(LOGIN_SCREEN){
            popUpTo(popUpTo){
                inclusive = isInclusive
            }
        }
    }

    val navigateToSignUp : (
        popUpTo:String, inclusive:Boolean
            ) -> Unit = {
                popTo, isInclusive ->
        navController.navigate(SIGN_UP_SCREEN){
            popUpTo(popTo){
                inclusive = isInclusive
            }
        }
    }
    val navigateBack: () -> Unit = {
        navController.navigateUp()
    }

    val navigateToProfile :(username:String) -> Unit = {
        navController.navigate("profile/${it}")
    }

    val navigateToMusic: () -> Unit = {
        navController.navigate(MUSIC_SCREEN_NAME){
            popUpTo(HOME_SCREEN_NAME)
        }
    }

    val navigateToProfileUpdate: () -> Unit = {
        navController.navigate(PROFILE_UPDATE_SCREEN)
    }

    val navigateToUploadDashboard: () -> Unit = {
        navController.navigate(UPLOAD_DASHBOARD_SCREEN)
    }
    val navigateToUploadFile: () -> Unit = {
        navController.navigate(UPLOAD_FILE_SCREEN)
    }
    val navigateToPlayer :(String) -> Unit =  {
        navController.navigate("player/$it")
    }
    val navigateToSecondOnboardScreen : () -> Unit =  {
        navController.navigate(ONBOARD_SECOND_SCREEN)
    }

    val navigateToThirdOnboardScreen : () -> Unit =  {
        navController.navigate(ONBOARD_THIRD_SCREEN)
    }



}