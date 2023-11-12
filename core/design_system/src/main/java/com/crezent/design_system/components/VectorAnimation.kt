package com.crezent.design_system.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun VectorAnimation(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
    description:String,
    finishedAnimation: () -> Unit = {}
){



    val atEnd = remember {
        mutableStateOf(false)
    }

    val image = AnimatedImageVector.animatedVectorResource(resId)

    LaunchedEffect(true){
        delay(100)
        atEnd.value = true
        finishedAnimation()
    }
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = image,
        atEnd = atEnd.value
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
              modifier = modifier.size(100.dp),
              painter =painter,
              contentDescription = "Icon"
          )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = description,
                fontSize = 20.sp,
                color = Color.White,

            )
        }
    }
}

@Preview
@Composable
private fun previewResult(){
//    VectorAnimation(
//        resId = R.drawable.okmark,
//        description = "Error Occurs"
//    )
}