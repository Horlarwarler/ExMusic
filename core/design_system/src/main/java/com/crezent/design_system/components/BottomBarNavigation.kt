package com.crezent.design_system.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.common.util.Constant
import com.crezent.design_system.theme.ExMusicTheme

@Composable
fun BottomBarNavigation(
    modifier: Modifier = Modifier,
    currentRoute: String = Constant.HOME_SCREEN_NAME,
    onNavigate: (selectedRoute:String) -> Unit
){


    val items = listOf<NavItemModel>(
        NavItemModel(Constant.HOME_SCREEN_NAME, R.drawable.home_round, "Home"),
        NavItemModel(Constant.UPLOAD_DASHBOARD_SCREEN, R.drawable.explorer, "Creator"),
        NavItemModel(Constant.MUSIC_SCREEN_NAME, R.drawable.music_round, "Music"),
//        NavItemModel(Constant.SETTINGS_SCREEN_NAME, R.drawable.settings_round, "Settings"),
    )
    Column(
        modifier = modifier
    ) {
        Divider(
            modifier = Modifier.height(1.dp),
            color = MaterialTheme.colorScheme.surface
        )
        BottomAppBar(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.background,

        ) {
            items.forEach {
                    item->
                //val backStackEntry = navcontroller.currentBackStackEntryAsState()

                //    val destinationRoute = backStackEntry.value?.destination?.route
                val selected = currentRoute==item.route
                BottomNavigationItem(
                    selected = selected,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                    onClick = {
                        if (currentRoute != item.route){
                            Log.d("Route","Cur")
                            onNavigate(item.route)
                        }
                    },
                    icon = {
                       val background = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        Icon(
                            painter = painterResource(id = item.icon) ,
                            contentDescription =item.text,
                            tint = background
                        )

                    }
                )
            }

        }

    }

    
   
}
private data class NavItemModel(
    val route: String,
    val icon:Int,
    val text: String,
)


@Preview
@Composable
private fun Preview(){
    ExMusicTheme() {
        BottomBarNavigation(onNavigate = {})

    }
}