package com.crezent.talimlectures.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.crezent.talimlectures.R
import kotlinx.coroutines.delay

@Composable
fun RecycleBin(){


    val recycleIsOpen = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true){
        delay(300)
        recycleIsOpen.value = true
        delay(800)
        recycleIsOpen.value = false
    }
    val offset by animateDpAsState(
        targetValue = if(recycleIsOpen.value) (-50).dp else 45.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ){
        Column(

            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Image(
                modifier = Modifier
                    .offset(y = offset)
                    .size(120.dp)
                ,
                painter = painterResource(R.drawable.top),
                contentDescription = "Top",

                )
            Image(
                modifier= Modifier.size(120.dp),
                painter = painterResource(R.drawable.bottom),
                contentDescription = "Bottom",

                )

        }

    }

}

@Preview
@Composable
private fun previewRecycleBin(){
    RecycleBin()
}