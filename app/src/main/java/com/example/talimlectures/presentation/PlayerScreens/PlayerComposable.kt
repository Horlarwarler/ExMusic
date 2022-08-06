package com.example.talimlectures.Destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navArgument

import com.example.talimlectures.presentation.PlayerScreens.Player
import com.example.talimlectures.util.Constant.NAVIGATION_ANIMATION_DURATION
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_ARGUMENT
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_NAME
import com.google.accompanist.navigation.animation.composable

//import androidx.navigation.*

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.PlayerComposable(
    navController: NavHostController,
){
    composable(
        route = PLAYER_SCREEN_NAME,

        enterTransition = {_,_->
            slideInHorizontally(
                initialOffsetX = {it},
                animationSpec = tween(
                    durationMillis = NAVIGATION_ANIMATION_DURATION,
                    easing = LinearOutSlowInEasing

                )
            )

        },
        exitTransition = {_,_->
            slideOutHorizontally(
                targetOffsetX = {it},
                animationSpec = tween(
                    durationMillis = NAVIGATION_ANIMATION_DURATION,
                    easing = FastOutLinearInEasing
                )
            )

        }
    ){

        Player(navController =navController,  )
    }
}