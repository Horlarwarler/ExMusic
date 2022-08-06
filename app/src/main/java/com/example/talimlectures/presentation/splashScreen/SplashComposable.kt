package com.example.talimlectures.presentation.splashScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

import com.example.talimlectures.util.Constant.SPLASH_SCREEN_NAME
import com.example.talimlectures.viewModels.SharedViewModel

@OptIn(ExperimentalAnimationApi::class)

// Adding an extension function on NavGraphBuilder
fun NavGraphBuilder.SplashComposable(
    //passing arguments
    navigateToLecture:() -> Unit,
    sharedViewModel: SharedViewModel,

    ){

    // creating our composable
    // composable contains our Composables
    composable(
        // The name of the screen
        route = SPLASH_SCREEN_NAME,
        // Animation provided to us by the accompanist library
        exitTransition = {_,_ ->
            slideOutVertically(
                targetOffsetY = {-it},
                animationSpec = tween(
                    durationMillis = 3000
                )

            )
        }
    ){
        //Calling the splash screen composable function
        SplashScreen (navigateToLecture = navigateToLecture, sharedViewModel = sharedViewModel)
    }
}