package com.example.talimlectures.Destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.example.talimlectures.presentation.homeScreen.HomeScreen
import com.example.talimlectures.util.Constant
import com.example.talimlectures.viewModels.SharedViewModel

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.HomeComposable(
    navigateToLecture:() -> Unit,
    navController: NavHostController,
    navigateToPlayer: (previousScreenName:String) -> Unit
){
    composable(
        route = Constant.HOME_SCREEN_NAME,
    ){
        HomeScreen(
            navController = navController,
            navigateToLecture = navigateToLecture,
            navigateToPlayer = navigateToPlayer
        )
    }
}