package com.example.talimlectures.util

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME
import com.example.talimlectures.util.Constant.SPLASH_SCREEN_NAME

class Screens(navController: NavHostController) {
    val navigateFromSplash:() -> Unit = {
        navController.navigate(LECTURE_SCREEN_NAME){
            popUpTo(SPLASH_SCREEN_NAME){
                inclusive = true
            }
        }
    }
    val navigateToLectures:()->Unit = {
        navController.navigate(LECTURE_SCREEN_NAME){
            popUpTo(LECTURE_SCREEN_NAME){
                inclusive = true
            }

        }
    }
    val navigateToMusic:(lectureId:Int)->Unit = {
        lectureId ->
        navController.navigate(route = "player/${lectureId}")
    }
}