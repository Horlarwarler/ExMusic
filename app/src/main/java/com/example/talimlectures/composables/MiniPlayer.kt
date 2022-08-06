package com.example.talimlectures.composables

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.talimlectures.R

import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.PlayAction

@Composable
fun MiniPlayer(
    miniPlayerVisible:Boolean,
    @DrawableRes miniPlayerIcon:Int,
    lecture: DatabaseLectureModel?,
    currentTime:Double,
    totalTime: Double,
    onActionClicked:(playAction: PlayAction)->Unit,
    onCloseButtonClicked:()->Unit,
    navigateTo: (previousScreenName: String) -> Unit,
    displayCurrentTime:String,
    displayTotalTime: String,
    navController: NavHostController

){
    if(lecture != null){
        if(miniPlayerVisible){
            Column(modifier = Modifier
                .fillMaxWidth()
                .background(MiniPlayerBackgroundColor)
                .clickable {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                    currentRoute?.let { navigateTo(it) }
                }
            ) {
                MyCanvas(totalTime = totalTime, currentTime =currentTime )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp, end = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = displayCurrentTime, fontSize = 10.sp, color = TextColor)
                    Text(text = displayTotalTime, fontSize = 10.sp,color = TextColor)

                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 1.dp, bottom = 5.dp, end = 20.dp)

                ) {
                    Box(
                        modifier = Modifier
                            .weight(1.5f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.close),
                            contentDescription = "Close",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onCloseButtonClicked() }
                            ,
                            tint = IconColor

                        )
                    }

                    Column(
                        modifier = Modifier
                            .weight(6f)
                            .fillMaxWidth()

                    ) {
                        Text(
                            text =lecture.lectureTitle,
                            fontSize = 13.sp,
                            color = TitleColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = lecture.lectureDescription,
                            fontSize = 10.sp,
                            color = DescriptionColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                            .weight(5.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly) {
                        IconButton(onClick = {
                            onActionClicked(PlayAction.REWIND)
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.rewind),
                                contentDescription = "Rewind",
                                modifier = Modifier.size(30.dp,25.dp),
                                tint = IconColor
                            )
                        }
                        IconButton(onClick = {
                            //Here it has Paused
                            if (miniPlayerIcon == R.drawable.play_icon) {
                                onActionClicked(PlayAction.PLAY)
                            }
                            //Here it is playing
                            else if(miniPlayerIcon == R.drawable.pause_icon){
                                onActionClicked(PlayAction.PAUSE)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = miniPlayerIcon),
                                contentDescription = "play",
                                modifier = Modifier.size(30.dp),
                                tint = IconColor

                            )
                        }
                        IconButton(onClick = { onActionClicked(PlayAction.FORWARD) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.forward),
                                contentDescription = "forward",
                                modifier = Modifier.size(30.dp,25.dp),
                                tint = IconColor

                            )
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun MyCanvas(
    totalTime:Double,
    currentTime: Double
){

    Box(
        modifier = Modifier
            .height(9.dp)
            .fillMaxWidth(1f),
        contentAlignment = Alignment.Center){
        val playerLength by animateFloatAsState(
            targetValue =currentTime.toFloat()/totalTime.toFloat(),
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            )

        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth(1f)
                .background(Color.White)

        ) {
             val width = size.width * playerLength
            drawLine(
                color = SelectedCategoryColor,
                start = Offset(x = 0F, y = 0F),
                end = Offset(x = width, y = 0f),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round

            )
            drawPoints(
                listOf(Offset(x = width, y = 0f)),
                pointMode = PointMode.Points,
                color = SelectedCategoryColor,
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round

            )

        }
    }

}

@Preview
@Composable fun previewMin(){
    MyCanvas(totalTime = 4.0, currentTime = 2.0)
}

