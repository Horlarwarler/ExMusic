package com.example.talimlectures.Destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.example.talimlectures.presentation.LectureScreens.LectureScreen
import com.example.talimlectures.util.Constant.HOME_SCREEN_NAME
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME
import com.example.talimlectures.util.Constant.NAVIGATION_ANIMATION_DURATION
import com.example.talimlectures.viewModels.SharedViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.LectureComposable(
    navigateToPlayer: (previousScreenName: String) -> Unit,
    navController: NavHostController,
    navigateToHome:()->Unit
){
    composable(
        route = LECTURE_SCREEN_NAME,
        enterTransition = {initial,_->
            if(initial.destination.route == HOME_SCREEN_NAME){
                slideInHorizontally(
                    initialOffsetX = {it},
                    animationSpec = tween(
                        durationMillis = NAVIGATION_ANIMATION_DURATION
                    )
                )
            }
            else{
                null
            }

        },
        exitTransition = { _, target ->
            if (target.destination.route == HOME_SCREEN_NAME){
                slideOutHorizontally(
                    targetOffsetX = {it},
                    animationSpec = tween(
                        durationMillis = NAVIGATION_ANIMATION_DURATION
                    )
                )
            }
            else{
                null
            }
        }
    ){
        LectureScreen(
            navController = navController,
            navigateToHome = navigateToHome,
            navigateToPlayer = navigateToPlayer
        )
    }
}