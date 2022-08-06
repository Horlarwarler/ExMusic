package com.example.talimlectures.presentation.splashScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talimlectures.ui.theme.BackgroundColor
import com.example.talimlectures.ui.theme.IconColor

import com.example.talimlectures.util.Constant.LOGO_ANIMATION_DURATION
import com.example.talimlectures.util.Constant.SPLASH_SCREEN_ANIMATION_DURATION
import com.example.talimlectures.viewModels.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    sharedViewModel: SharedViewModel,
    navigateToLecture:()-> Unit
){
    // By default the animation status should be false by default
    var animationStatus by remember {
        mutableStateOf(false)
    }
    // This specify the appnetworkState
    // By default it should be TRUE!
    var networkState by remember {
        mutableStateOf(false)
    }
    // Check if lecture in local database is empty
    val lectureNotEmpty = sharedViewModel.allLectures.value.isNotEmpty()
    // Initializing  network state of the app
    sharedViewModel.getNetworkState(LocalContext.current)
    // Getting the network state of the app
    networkState= sharedViewModel.networkState.value

    // if the app network is okay or if the user already has some information in his local database
    // Then checkComplete will return true
    val checkComplete = networkState|| lectureNotEmpty
    // Checking the database action
    val databaseAction = sharedViewModel.databaseAction.value
    //By default the databaseAction is none
    // So when it changes the launched this coroutine
    LaunchedEffect(key1 =databaseAction){
        sharedViewModel.handleDatabaseAction(databaseAction)
    }
    // It will launched this for the first an only if the checkComplete value changes
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true, key2 = checkComplete){
        sharedViewModel.getAllLectures()
        scope.launch {
            sharedViewModel.checkAllLecture()

        }
        // Changing the animation status to true
        animationStatus = true
        // Delay the animation duration
        delay(timeMillis = SPLASH_SCREEN_ANIMATION_DURATION)
        // This will only be executed if checkComplete is true
        if (checkComplete){
            // Navigate to lecture is called
            navigateToLecture()
        }
    }


    // Animating the logo in  position
    val offsetState by animateDpAsState(
        //By default animation status is false so then changing it to true
        // This will be animated from 200.dp to 0.DP
        targetValue = if(animationStatus)0.dp else 200.dp,
        //Animation spec 
        animationSpec = tween(
            durationMillis = LOGO_ANIMATION_DURATION
        )

    )
    val alphaState by animateFloatAsState(
        targetValue = if(animationStatus)1f else 0f,
        animationSpec = tween(
            durationMillis = LOGO_ANIMATION_DURATION
        )

    )
    val loadingState by animateFloatAsState(
        targetValue =if(animationStatus)1f else 0f,
        animationSpec = tween(
            delayMillis = LOGO_ANIMATION_DURATION
        )
    )


    Box(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize(),
    ) {
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .offset(y = offsetState)
                .alpha(alphaState)
                .align(Alignment.Center),
            tint = IconColor
        )
        WaitingIcon(
            modifier = Modifier
                .size(30.dp)
                // .padding(bottom =20.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)

                .alpha(loadingState),
            enabled =!checkComplete)
    }
}

@Preview
@Composable
private fun PreviewSplash(){

}

@Composable
fun WaitingIcon(
    enabled: Boolean,
    modifier:Modifier
){
    if (enabled) {
        CircularProgressIndicator(
            modifier = modifier,
            strokeWidth = 2.dp,
            color= Color.White
        )
    }

}