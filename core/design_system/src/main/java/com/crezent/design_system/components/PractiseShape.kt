package com.crezent.design_system.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class TicketShape(private val cornerRadius: Float) : Shape{
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return  Outline.Generic(
            path = drawTicketWithCorner(size, cornerRadius)
        )

    }

}

fun drawTicketWithCorner(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()

        arcTo(
            Rect(
                left = -cornerRadius,
                right = cornerRadius,
                top = -cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90F,
            sweepAngleDegrees = -90F,
            forceMoveTo = false
        )
        lineTo(x = size.width- cornerRadius, y = 0F)
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y=size.height-cornerRadius)

        arcTo(
            Rect(
                left =size.width- cornerRadius,
                right =size.width+ cornerRadius,
                top = size.height -cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270F,
            sweepAngleDegrees = -90F,
            forceMoveTo = false
        )
        lineTo(x = 0F+cornerRadius, y=size.height)
        arcTo(
            Rect(
                left =-cornerRadius,
                right =cornerRadius,
                top = size.height -cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0F,
            sweepAngleDegrees = -90F,
            forceMoveTo = false
        )
        lineTo(x = 0F, y=cornerRadius)
        close()
    }
}


@Composable
fun TicketComposable(modifier: Modifier = Modifier) {
    Text(
        text = "🎉 CINEMA TICKET 🎉",
        style = TextStyle(
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .wrapContentSize()
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = TicketShape(24.dp.toPx())
                clip = true
            }
            .background(color = Color.DarkGray)
            .drawBehind {
                scale(scale = 0.9f) {
                    drawPath(
                        path = drawTicketWithCorner(size = size, cornerRadius = 24.dp.toPx()),
                        color = Color.Red,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                        )
                    )
                }
            }
            .padding(start = 32.dp, top = 64.dp, end = 32.dp, bottom = 64.dp)
    )
}

@Composable
private fun MusicShimmer(
    barWidth:Float,
    barGap:Float,
    isAnimating:Boolean
){
    val heightDivider by animateFloatAsState(
        targetValue = if (isAnimating)1F else 6F, label = "",
        animationSpec = tween(1000, easing =  LinearEasing)
        )
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animations = mutableListOf<State<Float>>()

    repeat(25){
        animations+= infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 1F,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    Random.nextInt(500,2000),

                    ),
                repeatMode = RepeatMode.Reverse,

            ), label = "Animation"
        )
    }


    val initialMultipliers =  remember {
        mutableListOf<Float>().apply {
           repeat(25){
               val number = Random.nextFloat()
               this += number

           }
        }

    }

    Canvas(
        modifier = Modifier
            .height(50.dp)
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
    ){
        val canvasWidth = size.width
        val canvasHeight = size.height

        val count = (canvasWidth/(barWidth+barGap)).toInt().coerceAtMost(25)
        val minHeight = 0F
        val maxHeight = canvasHeight/2F/heightDivider
        var startOffset = ( canvasWidth -  count *(barWidth + barGap))/2
        repeat(count){index ->
            val currentSize = animations[index].value
            var barHeightPercent = initialMultipliers[index] + currentSize
            if (barHeightPercent > 1.0f) {
                val diff = barHeightPercent - 1.0f
                barHeightPercent = 1.0f - diff
            }

            val barHeight = lerp(minHeight.dp,maxHeight.dp,barHeightPercent).value
            drawLine(
                color = Color.Black,
                strokeWidth = barWidth,
                start = Offset(startOffset, (center.y - barHeight/2)),
                end = Offset(startOffset, (center.y + barHeight/2)),
                cap = StrokeCap.Round
            )
            startOffset += (barWidth + barGap)

        }

    }
}

@Composable
private fun ExpandAnimation(){
    var isEnabled by remember {
        mutableStateOf(false)
    }

    BoxWithConstraints(
        modifier = Modifier
            .clickable { isEnabled = true }
            .fillMaxWidth()
            .height(100.dp)
    ) {

        val density = LocalDensity.current
        val maxWidth = with(density){
                constraints.maxWidth.toDp()
        }
        val maxHeight = with(density){
            constraints.maxHeight.toDp()
        }
        val cornerDuration = 3000
        val expandDuration = 6000
        val updateTransition = updateTransition(targetState =isEnabled, label = "Animation" )
        val durationMillis = cornerDuration+ expandDuration
         val minWidth = 10.dp


        val animatedHeight = updateTransition.animateDp(
            transitionSpec = {
                keyframes {
                    if (targetState){
                        maxHeight at cornerDuration
                    }
                    else {
                        maxHeight at expandDuration
                        minWidth at durationMillis
                    }
                }

            }, label = "Height Animation"
        ) {

            if (it){
                 maxHeight
            }
            else {
                minWidth
            }
        }
        // Animate the width
        val animatedWidth = updateTransition.animateDp(
            transitionSpec = {
                 keyframes {
                     if (targetState){
                         maxHeight at cornerDuration
                         maxWidth at durationMillis
                     }
                     else{
                        maxHeight at expandDuration
                         minWidth at durationMillis
                     }
                 }
            },
            label = "Width Animation"){
                if (it){
                    maxWidth
                }
            else{
                minWidth
            }
        }


        val alpha = remember {
            (animatedWidth.value - minWidth)/ (maxWidth - minWidth)
        }



        Card(
            modifier = Modifier
                .clickable {
                    isEnabled = !isEnabled
                }
                .background(Color.Green.copy(alpha = alpha), RoundedCornerShape(15.dp))
                .height(animatedHeight.value)
                .width(animatedWidth.value)
            ,
            shape = RoundedCornerShape(5.dp)
        ) {
            repeat(4){
               // Icon(painter = painterResource(id = R.drawable.error), contentDescription =  "hello")
            }

        }




    }
}

@Preview
@Composable
private fun Preview(){
   // MusicShimmer(barWidth = 10f, barGap =10f , isAnimating =true )

}