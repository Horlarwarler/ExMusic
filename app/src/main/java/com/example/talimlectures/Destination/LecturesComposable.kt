package com.example.talimlectures.Destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.talimlectures.Screens.LectureScreens.LectureScreen
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME

fun NavGraphBuilder.LectureComposable(
    navigateToPlayer:(lectureId:Int)-> Unit
){
    composable(
        route = LECTURE_SCREEN_NAME,
    ){
        LectureScreen()
    }
}