package com.crezent.talimlectures.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.crezent.design_system.components.ShimmerItem

@Composable
fun LoadingScreenShimmer(
    padding: Dp = 16.dp
){

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val screenWidth  = with(LocalDensity.current){
            (maxWidth - 2 * padding).toPx()
        }
        val screenHeight = with(LocalDensity.current){
            (maxHeight - 2 * padding).toPx()
        }

        val gradientWidth:Float = 0.055F * screenHeight
        val xAnimation = rememberInfiniteTransition()
        val yAnimation = rememberInfiniteTransition()

        val xPosition = xAnimation.animateFloat(
            initialValue = 0F,
            targetValue = screenWidth + gradientWidth,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearOutSlowInEasing,
                    delayMillis = 150
                ),
                repeatMode = RepeatMode.Restart
            )

        )

        val yPosition = yAnimation.animateFloat(
            initialValue = 0F,
            targetValue = screenHeight + gradientWidth,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing,
                    delayMillis = 150
                ),
                repeatMode = RepeatMode.Restart
            )

        )
        val colors = listOf(
            MaterialTheme.colors.primary,
            MaterialTheme.colors.primary.copy(alpha = 0.35F),
            MaterialTheme.colors.primary,
        )

        com.crezent.design_system.components.ShimmerItem(
            xPosition = xPosition.value,
            yPosition = yPosition.value,
            colors = colors,
            gradientWidth = gradientWidth,
            height = screenHeight.dp,
            width = screenWidth.dp
        )

    }
}


@Preview
@Composable
private fun previewScreen(){
    LoadingScreenShimmer()
}