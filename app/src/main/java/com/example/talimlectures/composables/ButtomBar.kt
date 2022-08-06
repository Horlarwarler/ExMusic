package com.example.talimlectures.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.talimlectures.R
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.Constant.HOME_SCREEN_NAME
import com.example.talimlectures.util.Constant.LECTURE_SCREEN_NAME
import com.example.talimlectures.util.Constant.PLAYER_SCREEN_NAME
import com.example.talimlectures.util.NavItemData
import com.example.talimlectures.util.PlayAction

@Composable
fun BottomBar(
    navcontroller:NavHostController,
    lecture:DatabaseLectureModel?,
    onClick: () -> Unit,
    miniPlayerVisible:Boolean,
    @DrawableRes miniPlayerIcon:Int,
    currentTime: Double,
    onActionClicked:(playAction: PlayAction)->Unit,
    onCloseButtonClicked:()->Unit,
    navigate: (previousScreenName: String) -> Unit,
    totalTime:Double,
    displayCurrentTime:String,
    displayTotalTime: String

){

    Column(modifier = Modifier.fillMaxWidth()) {
        MiniPlayer(miniPlayerVisible =miniPlayerVisible ,
            miniPlayerIcon =miniPlayerIcon ,
            lecture =lecture ,
            currentTime = currentTime,
            onActionClicked =onActionClicked,
            onCloseButtonClicked = onCloseButtonClicked,
            navigateTo = navigate,
            totalTime = totalTime,
            displayCurrentTime = displayCurrentTime,
            displayTotalTime = displayTotalTime,
            navController = navcontroller
        )
        BottomBarNavigation(navcontroller = navcontroller, onClick = onClick)

    }

}

@Composable
fun BottomBarNavigation(
    navcontroller: NavHostController,
    onClick: () -> Unit
){
    val items = listOf(
        NavItemData(HOME_SCREEN_NAME,R.drawable.home,"Home"),
        NavItemData(LECTURE_SCREEN_NAME,R.drawable.ic_baseline_music_note_24,"Lectures")
    )
    BottomNavigation(
        backgroundColor = BackgroundColor,
        modifier = Modifier.fillMaxWidth(),
        elevation = AppBarDefaults.BottomAppBarElevation,

        )
    {

        items.forEach {
                item->
            val backStackEntry = navcontroller.currentBackStackEntryAsState()

            val destinationRoute = backStackEntry.value?.destination?.route
            val selected = destinationRoute==item.route
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = SelectedCategoryColor,
                unselectedContentColor = SearchTextColor,
                onClick = {
                    if (destinationRoute!! == PLAYER_SCREEN_NAME){
                        navcontroller.navigate(item.route)
                    }
                    else if (destinationRoute != item.route){
                        onClick()
                    }
                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(id = item.icon) , contentDescription =item.text )
                        Text(text = item.text, fontSize = 13.sp)
                    }
                }
            )
        }
    }
}