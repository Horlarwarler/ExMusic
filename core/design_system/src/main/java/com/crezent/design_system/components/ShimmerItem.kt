package com.crezent.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp


@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier,
    xPosition:Float,
    yPosition: Float,
    gradientWidth: Float,
    height: Dp,
    width:Dp,
    colors:List<Color>,
    animateY:Boolean = true
){
    val startOffset = Offset(
        xPosition-gradientWidth,
        yPosition-gradientWidth
    )
    val endOffset = Offset(
        xPosition,
        if(animateY)yPosition else yPosition-gradientWidth
    )

    val brush = Brush.linearGradient(
        start = startOffset,
        end = endOffset,
        colors = colors
    )
    Surface(
    ){
        Spacer(
            modifier = modifier
                .height(height)
                .width(width)
                .background(brush)
        )
    }
}

//@Preview
//@Composable
//fun previewShimmer(){
//    val colors = listOf<Color>(
//        darkPrimary,
//        darkPrimary.copy(alpha = 0.3f),
//        darkPrimary
//    )
//    ShimmerItem(
//        xPosition = 300F,
//        yPosition = 3000F,
//        gradientWidth = 10f,
//        height = 100.dp,
//        width = 100.dp,
//        colors = colors
//    )
//}