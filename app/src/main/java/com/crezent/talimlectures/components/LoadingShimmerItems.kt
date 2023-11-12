package com.crezent.talimlectures.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun ColumnShimmerItems(
    height: Dp = 60.dp,
    padding:Dp = 10.dp
){

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
       val width = with(LocalDensity.current){
           (maxWidth - (2 * padding)).toPx()
       }
       val gradientWidth:Float = 0.25F * width
        val xAnimation = rememberInfiniteTransition()
        val yAnimation = rememberInfiniteTransition()

        val xPosition = xAnimation.animateFloat(
            initialValue = 0F,
            targetValue = width + 2*gradientWidth,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(
                    durationMillis = 1250,
                    easing = LinearEasing,
                    delayMillis = 300

                ),
                repeatMode = RepeatMode.Restart
            )

        )
        val colors = listOf(
            MaterialTheme.colors.background,
            MaterialTheme.colors.background.copy(alpha = 0.5f),
            MaterialTheme.colors.background
        )

        val yPosition = yAnimation.animateFloat(
            initialValue = 0F,
            targetValue = height.value + 2*gradientWidth,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            )

        )
        Column(
            modifier = Modifier.padding(end = 5.dp)
        ){
            repeat(10){
                index ->
                com.crezent.design_system.components.ShimmerItem(
                    xPosition = xPosition.value,
                    yPosition = height.value,
                    height = height,
                    width = width.dp,
                    gradientWidth = gradientWidth,
                    animateY = false,
                    colors = colors
                )
                Spacer(modifier = Modifier
                    .height(5.dp)
                    .fillMaxWidth()
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = MaterialTheme.colors.primary
                )
            }
        }



    }
}