package com.crezent.user_presentation.playerScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.crezent.design_system.components.CircularProgress
import com.crezent.design_system.theme.smallPadding
import com.crezent.common.util.MusicIconEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PlayerScreenButtons(
    modifier: Modifier = Modifier,
    isPlaying:Boolean = false,
    repeatMode:Int = Player.REPEAT_MODE_OFF,
    isShuffled:Boolean = false,
    playerState:Int = Player.STATE_IDLE,
    showHideTimer:() -> Unit,
    musicAction: (MusicIconEvent) -> Unit,

    ){
    val shuffleIconColor = if (isShuffled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
    val loopIconColor = if (repeatMode == Player.REPEAT_MODE_OFF ) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primary
    val repeatText = when (repeatMode){
        Player.REPEAT_MODE_OFF -> {
            "Repeat Off"
        }
        Player.REPEAT_MODE_ONE ->{
            "Repeat Current"
        }
        Player.REPEAT_MODE_ALL ->{
            "Repeat All"
        }

        else -> {
            " Repeat Off"
        }
    }
    val playerLoading = playerState == Player.STATE_BUFFERING
    val playerEnded = playerState == Player.STATE_ENDED
    val playerReady = playerState == Player.STATE_READY
    var job: Job? = null
    val playIcon =    when {
        playerEnded && repeatMode != Player.REPEAT_MODE_ONE -> {
           com.crezent.design_system.R.drawable.baseline_restart_alt_24
        }
        isPlaying ->{
            com.crezent.design_system.R.drawable.pause
        }
        !isPlaying ->{
            com.crezent.design_system.R.drawable.play
        }

        else -> {
            com.crezent.design_system.R.drawable.play

        }
    }
    LaunchedEffect(key1 = playerEnded){
        if (playerEnded && repeatMode!= Player.REPEAT_MODE_ONE){
            job = CoroutineScope(Dispatchers.IO)
                .launch {
                    delay(5000)
                    musicAction(MusicIconEvent.NEXT)
                    cancel()
                }
            job?.invokeOnCompletion {
                showHideTimer()
            }

        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp, start = smallPadding, end = smallPadding)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
                musicAction(MusicIconEvent.REPEAT)
            }) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    painter = painterResource(id =    com.crezent.design_system.R.drawable.loop),
                    contentDescription = "Loop",
                    tint = loopIconColor

                )
                Text(
                    text = repeatText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }

        IconButton(
            onClick = {
                musicAction(MusicIconEvent.PREVIOUS)

            }) {
            Icon(
                painter = painterResource(id =    com.crezent.design_system.R.drawable.previous),
                contentDescription = "Loop",
                tint = MaterialTheme.colorScheme.onBackground

            )
        }
        if (playerLoading){
             CircularProgress()
        }
        else{

            IconButton(
                onClick = {
                    if (playerEnded && repeatMode != Player.REPEAT_MODE_ONE){
                        musicAction(MusicIconEvent.NEXT)

                        job?.cancel()
                    }
                    else {
                        musicAction(MusicIconEvent.PLAY_PAUSE)
                    }
                },
            ) {
                Icon(
                    painter = painterResource(id = playIcon),
                    contentDescription = "play",
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(com.crezent.design_system.theme.smallSpacer)
                    ,
                    tint = MaterialTheme.colorScheme.onPrimary

                )
            }
        }


        IconButton(
            onClick = {
                musicAction(MusicIconEvent.NEXT)

            }) {
            Icon(
                painter = painterResource(id =    com.crezent.design_system.R.drawable.next),
                contentDescription = "Loop",
                tint = MaterialTheme.colorScheme.onBackground

            )
        }
        IconButton(
            onClick = {
                musicAction(MusicIconEvent.SHUFFLE)
            }) {
            Icon(
                painter = painterResource(id =    com.crezent.design_system.R.drawable.shuffle),
                contentDescription = "Loop",
                tint = shuffleIconColor,
            )
        }

    }

}