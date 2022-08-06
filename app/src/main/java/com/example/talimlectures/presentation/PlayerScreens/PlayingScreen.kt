package com.example.talimlectures.presentation.PlayerScreens

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.talimlectures.R
import com.example.talimlectures.composables.BottomBarNavigation
import com.example.talimlectures.composables.PlayingAppBar
import com.example.talimlectures.ui.theme.*
import com.example.talimlectures.util.PlayAction


@Composable

fun Player(
    navController: NavHostController,
    playingScreenModel: PlayingScreenModel = hiltViewModel(),

    ){
    val previousScreenName = playingScreenModel.playingScreenState.previousScreenName!!
    Scaffold(
        backgroundColor = BackgroundColor,
        topBar = {
                 PlayingAppBar (onBackButtonClicked = {
                     navController.navigate(previousScreenName){
                         popUpTo(previousScreenName){
                             inclusive = true
                         }
                     }
                 })
        },
        content = {
                  PlayingScreen(
                      miniPlayerIcon = if(playingScreenModel.musicState.isPaused)R.drawable.play_icon else R.drawable.pause_icon,
                      onActionClicked ={

                          playingScreenModel.miniPlayerAction(it)
                      },
                      onFavouriteClick = {
                          playingScreenModel.handleChangeEvent(PlayingScreenEvent.OnFavouriteClick(it))
                      },
                      playingScreenModel = playingScreenModel
                  )
        },
        bottomBar = {
                BottomBarNavigation( navcontroller =navController , onClick = {})
        }
    )
}


@Composable
fun PlayingScreen(

    @DrawableRes miniPlayerIcon:Int,
    onActionClicked:(playAction: PlayAction)->Unit,
    onFavouriteClick:(isFavourite:Boolean)->Unit,
    playingScreenModel: PlayingScreenModel

) {
    var current by remember {
        mutableStateOf(0f)
    }
    val currentTime = playingScreenModel.musicState.currentTime
    val totalTime = playingScreenModel.musicState.totalTime
    val displayTotalTime = playingScreenModel.musicState.totalTimeDisplay
    val displayCurrentTime = playingScreenModel.musicState.currentTimeDisplay
    val lecture = playingScreenModel.playingScreenState.currentLecture!!
    val currentLength by animateFloatAsState(targetValue = (currentTime / totalTime).toFloat())
    val configuration = LocalConfiguration.current.orientation
    val favourite = playingScreenModel.playingScreenState.favourites.firstOrNull {
        lecture.lectureTitle== it.lectureTitle
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp)
    ) {

        val isPortrait:Boolean = configuration != Configuration.ORIENTATION_LANDSCAPE

        val constrain = if(isPortrait)potraitConstraintSet() else LandScapeConstraintSet()
        val width = if (isPortrait)240.dp else 100.dp


            ConstraintLayout(constrain) {
                Box(modifier = Modifier
                    .size(width)
                    .layoutId("box")
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.music, ),
                        contentDescription = "Mosque",

                        modifier = Modifier
                            .fillMaxSize()
                            .border(3.dp, IconColor, CircleShape)
                            .align(Alignment.Center),
                        tint = IconColor

                    )
                    CircularProgressIndicator(
                        progress = currentLength,
                        modifier = Modifier.size(width),
                        strokeWidth = 3.2.dp,
                        color = SelectedCategoryColor
                    )
                }
                //Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier.layoutId("length"),
                    text = " $displayCurrentTime. | $displayTotalTime",
                    fontSize = 12.sp,
                    color = TextColor)
                // Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.layoutId("lecture"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = lecture.lectureTitle, fontSize = 19.sp, color = TextColor)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = lecture.lectureDescription, fontSize = 16.sp, color = TextColor)
                }

                //Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .layoutId("playAction"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = {
                        onActionClicked(PlayAction.PREVIOUS)
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.previous),
                            contentDescription = "Rewind",
                            modifier = Modifier.size(30.dp, 25.dp),
                            tint = IconColor
                        )
                    }
                    IconButton(onClick = {
                        //Here it has Paused
                        if (miniPlayerIcon == R.drawable.play_icon) {
                            onActionClicked(PlayAction.PLAY)
                        }
                        //Here it is playing
                        else if (miniPlayerIcon == R.drawable.pause_icon) {
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
                    IconButton(onClick = { onActionClicked(PlayAction.NEXT) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.next),
                            contentDescription = "forward",
                            modifier = Modifier.size(30.dp, 25.dp),
                            tint = IconColor

                        )
                    }
                    IconButton(
                        onClick = {
                            onFavouriteClick(favourite != null)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.favourite),
                            contentDescription = "forward",
                            modifier = Modifier.size(30.dp, 25.dp),
                            tint = if(favourite != null)FavouriteColor else IconColor

                        )
                    }
                }
            }

        }

}

private fun potraitConstraintSet():ConstraintSet{
    return ConstraintSet {
        val lecture = createRefFor("lecture")
        val playAction = createRefFor("playAction")

        val length = createRefFor("length")
      //  val rewind = createRefFor("rewind")
        val box = createRefFor("box")
        constrain(box){
            top.linkTo(parent.top)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end)
        }
        constrain(length){
            top.linkTo(box.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(lecture){
            top.linkTo(length.bottom, margin = 10.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
        constrain(playAction){
            top.linkTo(lecture.bottom)
            start.linkTo(parent.start, margin = 10.dp)
            end.linkTo(parent.end, margin = 10.dp)
           // bottom.linkTo(parent.bottom)
        }

    }
}
private fun LandScapeConstraintSet():ConstraintSet{
    return ConstraintSet {
        val lecture = createRefFor("lecture")
        val playAction = createRefFor("playAction")

        val length = createRefFor("length")
        //  val rewind = createRefFor("rewind")
        val box = createRefFor("box")
        constrain(box){
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(lecture.start)
        }
        constrain(length){
            top.linkTo(box.bottom, margin = 5.dp)
            start.linkTo(box.start)
            end.linkTo(box.end)
           // bottom.linkTo(parent.bottom)
        }

        constrain(lecture){
            top.linkTo(box.top)
            start.linkTo(box.end, margin = 10.dp)
            end.linkTo(playAction.start, margin = 10.dp)
            bottom.linkTo(box.bottom)
        }
        constrain(playAction){
            top.linkTo(parent.top)
            //start.linkTo(lecture.end)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
            width = Dimension.wrapContent
        }

        createHorizontalChain(box,lecture,playAction , chainStyle = ChainStyle.Spread )

    }
}