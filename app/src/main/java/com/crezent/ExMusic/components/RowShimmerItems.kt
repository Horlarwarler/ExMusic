package com.crezent.ExMusic.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowShimmerItems(
    width:Dp = 100.dp,
    height:Dp = 100.dp
){

    val gradientWidth:Float = 0.25F * height.value
    val xAnimation = rememberInfiniteTransition()
    val yAnimation = rememberInfiniteTransition()

    val xPosition = xAnimation.animateFloat(
        initialValue = 0F,
        targetValue = (width.value) * 3,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
                delayMillis = 150
            ),
            repeatMode = RepeatMode.Restart
        )

    )

    val yPosition = yAnimation.animateFloat(
        initialValue = 0F,
        targetValue = (height.value) * 3,
        animationSpec = InfiniteRepeatableSpec(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
                delayMillis = 150
            ),
            repeatMode = RepeatMode.Restart
        )

    )
    val colors = listOf<Color>(
        MaterialTheme.colors.background,
        MaterialTheme.colors.background.copy(alpha = 0.5f),
        MaterialTheme.colors.background
    )

    Row(
        modifier = Modifier.padding(end = 10.dp)
    ){

        repeat(10){
            com.crezent.design_system.components.ShimmerItem(
                xPosition = xPosition.value,
                yPosition = yPosition.value,
                gradientWidth = gradientWidth,
                height = height,
                width = width,
                colors = colors
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
    }
}