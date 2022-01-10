package com.example.talimlectures.Destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_ARGUEMENT
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_NAME

fun NavGraphBuilder.PlayerComposable(
    navigateToLecture:()-> Unit
){
    composable(
        route = PLAYER_SCREEN_NAME,
        arguments = listOf(navArgument(PLAYER_SCREEN_ARGUEMENT){
            type = NavType.IntType
        })
    ){

    }
}