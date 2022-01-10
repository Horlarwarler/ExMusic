package com.example.talimlectures.Screens.SplashScreens

import android.window.SplashScreen
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.talimlectures.R
import com.example.talimlectures.ui.theme.BackgroundColor
import com.example.talimlectures.ui.theme.IconColor
import com.example.talimlectures.ui.theme.LightBackground
import com.example.talimlectures.ui.theme.NightBackground
import com.example.talimlectures.util.Constant.LOGO_ANIMATION_DURATION
import com.example.talimlectures.util.Constant.SPLASH_SCREEN_ANIMATION_DURATION
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToLecture:()-> Unit
){
    var animationStatus by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true){
        animationStatus = true
        delay(timeMillis = SPLASH_SCREEN_ANIMATION_DURATION)
        navigateToLecture()

    }
    val offsetState by animateDpAsState(
        targetValue = if(animationStatus)0.dp else 200.dp,
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



    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.BackgroundColor)
            .fillMaxSize()

    ){
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .offset(y = offsetState)
                .alpha(alphaState),
            tint = MaterialTheme.colors.IconColor
        )
    }
}

@Preview
@Composable
private fun PreviewSplash(){
    SplashScreen {

    }
}