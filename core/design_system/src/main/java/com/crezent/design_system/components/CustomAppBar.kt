package com.crezent.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.appBarHeight
import com.crezent.design_system.theme.smallPadding

@Composable
fun CustomAppBar(
    modifier : Modifier = Modifier,
    leftAction: @Composable ()  -> Unit = {},
    middleAction: @Composable () -> Unit = {},
    rightAction:@Composable () -> Unit = {}
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(appBarHeight)
            .padding(vertical = smallPadding)

        ,
    ){
        //left center right
        //
        Box(
            modifier = Modifier.align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ){
            leftAction(

            )
        }
        Box(
            modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        {
           middleAction()
        }


        Box(
            modifier = Modifier.align(Alignment.CenterEnd),
            contentAlignment = Alignment.Center
        ){
            rightAction()
        }


    }
}
@Preview
@Composable
private fun PreviewAppBar(){
    ExMusicTheme() {
        CustomAppBar(
            leftAction = {  Icon(
            modifier = Modifier.clickable {

            },
            imageVector = ExMusicIcons.backButton,
            contentDescription = "Navigate Back",
        ) }
        ) {

        }
    }

}