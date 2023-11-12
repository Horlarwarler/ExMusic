package com.crezent.user_presentation.playerScreen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallSpacer
import com.crezent.user_presentation.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlayingIndicator(
    modifier: Modifier = Modifier,
    thumbnailUrl:String? = null,
    progress:Float = 0F,
    remainDuration:String,
    imageLoader: ImageLoader

){
    val painter = rememberAsyncImagePainter(
        model =thumbnailUrl ,
        imageLoader =imageLoader,
        placeholder = painterResource(id = R.drawable.background),
        error = painterResource(id = R.drawable.background)
        )


    val progressAnimation = animateFloatAsState(
        targetValue = (progress * 360.0).toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "Progress Animation")
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Box(
            modifier = modifier.size(250.dp),
            contentAlignment = Alignment.Center
        ){
            val backgroundColor = MaterialTheme.colorScheme.primaryContainer
            val progressColor = MaterialTheme.colorScheme.primary

            Image(
                modifier = Modifier
                    .padding(mediumPadding)
                    .drawBehind {
                        drawArc(
                            color =backgroundColor,
                            startAngle = -90F,
                            sweepAngle = 360F,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                        )
                        if (progress == 0F) {
                            return@drawBehind
                        }
                        drawArc(
                            color = progressColor,
                            startAngle = -90F,
                            sweepAngle = progressAnimation.value,
                            useCenter = false,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round),
                        )
                        val anglesInDegree = ((progress * 360.0))
                        val radius = size.height / 2

                        val x =
                            (radius * sin(Math.toRadians(progressAnimation.value.toDouble()))).toFloat() + (size.height / 2)
                        val y =
                            -(radius * cos(Math.toRadians(progressAnimation.value.toDouble()))).toFloat() + (size.height / 2)

                        drawCircle(
                            color = progressColor,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )


                    }
                    .padding(com.crezent.design_system.theme.smallSpacer)
                    .size(250.dp)

                    .clip(CircleShape)

                ,
                painter = painter,
                contentDescription = "Thumbnail Image" ,
                contentScale = ContentScale.Crop
            )

        }
        Text(
            text = "-$remainDuration",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }


}
