 package com.crezent.user_presentation.playerScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.crezent.common.util.MusicIconEvent
import com.crezent.common.util.currentTime
import com.crezent.design_system.ExMusicIcons
import com.crezent.design_system.components.CustomAppBar
import com.crezent.design_system.components.CustomDialog
import com.crezent.design_system.theme.ExMusicTheme
import com.crezent.design_system.theme.largePadding
import com.crezent.design_system.theme.mediumPadding
import com.crezent.design_system.theme.smallPadding
import com.crezent.models.Song
import com.crezent.models.TaskStatus
import com.crezent.user_presentation.playerScreen.components.PlayerScreenButtons
import com.crezent.user_presentation.playerScreen.components.PlayingIndicator


@Composable

fun PlayerScreen(
    state:PlayerScreenState,
    navigateBack: () -> Unit,
    downloadEvent:(com.crezent.download.DownloadEvent) ->Unit,
    musicAction: (MusicIconEvent) -> Unit,
    snackBarHostState: SnackbarHostState,
    imageLoader: ImageLoader,
    route:String,
    onNavigate:(route:String) -> Unit
){

  //  val state = viewModel.playerScreenState.collectAsState().value

//     val musicAction = viewModel::musicAction
//     val uiEvent = viewModel.uiEvent
    var showDialog  by remember {
        mutableStateOf(false)
    }
     val context = LocalContext.current

     val currentDownloadStatus =  state.currentDownload?.taskStatus
    val isCurrentDownload = state.currentDownload  != null
    val isDownloaded = state.isDownloaded
    val shouldShowProgress = isCurrentDownload && (currentDownloadStatus == TaskStatus.ONGOING || currentDownloadStatus == TaskStatus.PAUSED)
     val isPaused = isCurrentDownload &&  currentDownloadStatus== TaskStatus.PAUSED
     LaunchedEffect(key1 = true  ){
//         uiEvent.collectLatest {
//             if (it is com.crezent.common.util.UiEvent.ShowSnackBar){
//                 val message = it.message.asString(context)
//
//                 snackBarHostState.showSnackbar(message)
//
//             }
//         }
     }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = smallPadding, end = mediumPadding, bottom = mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomAppBar(
           // title = stringResource(id = R.string.player),
            leftAction = {
                Icon(
                    modifier = Modifier.clickable {
                        navigateBack()
                    },
                    imageVector = ExMusicIcons.backButton,
                    contentDescription = "Navigate Back",
                )

            },
            rightAction = {
                  PlayerActionStatus(
                      isDownloaded = state.isDownloaded,
                      isPaused = isPaused,
                      downloadProgress = state.currentDownload?.progress?.toFloat()?:0F,
                      showProgress = shouldShowProgress
                  ) {
                      showDialog = true
                  }
            },
        )
        var remainDuration = state.totalDuration - state.currentDuration
        var progress = state.currentDuration.toFloat()/state.totalDuration.toFloat()
        if (progress.isNaN()){
            progress = 0F
            remainDuration = 0
        }
        Spacer(modifier = Modifier.height(mediumPadding))
        PlayingIndicator(
            remainDuration = remainDuration.currentTime() ,
            progress = progress,
            thumbnailUrl = state.playingMediaItem?.thumbnail,
            imageLoader = imageLoader
        )
        Spacer(modifier = Modifier.height(mediumPadding))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${state.playingMediaItem?.title}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${state.playingMediaItem?.artist}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(largePadding))
        PlayerScreenButtons(
            modifier = Modifier,
            playerState = state.playerState,
            isPlaying = state.isPlaying,
            isShuffled = state.isShuffle,
            repeatMode = state.repeatMode,
            showHideTimer = {
            },
            musicAction = musicAction
        )
        if (showDialog){

            val title = when {
                state.isDownloaded  -> {
                    "Remove From Download"
                }
                isCurrentDownload && currentDownloadStatus == TaskStatus.ONGOING -> {
                    "Pause Or Stop Download"
                }
                isCurrentDownload && currentDownloadStatus == TaskStatus.PAUSED ->{
                    "Resume Download"
                }
                else -> {
                    "Download Media"
                }
            }
            val description = when {
                state.isDownloaded  -> {
                    "Do you want to remove this from download"
                }
                isCurrentDownload && currentDownloadStatus == TaskStatus.ONGOING -> {
                    "Media is Currently downloading, do you want to pause or cancel the download"
                }
                isCurrentDownload && currentDownloadStatus == TaskStatus.PAUSED ->{
                    "Resume Downloading this file"
                }
                else -> {
                    "Download this media on your device for offline playback"
                }
            }
            val confirmClick : () ->Unit = {
                showDialog = false
                when {
                    state.isDownloaded ->{
                        downloadEvent(com.crezent.download.DownloadEvent.RemoveFromDownload(state.playingMediaItem!!.songId!!))
                    }
                    isCurrentDownload && currentDownloadStatus == TaskStatus.ONGOING -> {
                        downloadEvent(com.crezent.download.DownloadEvent.PauseDownload(state.playingMediaItem!!.songId!!))
                    }
                    isCurrentDownload && currentDownloadStatus == TaskStatus.PAUSED ->{
                        downloadEvent(com.crezent.download.DownloadEvent.ResumeDownload(state.playingMediaItem!!.songId!!))
                    }
                    else -> {
                        downloadEvent(com.crezent.download.DownloadEvent.StartDownload(state.playingMediaItem!!.songId!!))
                    }
                }
            }
            CustomDialog(
                onDismissRequest = { showDialog = false },
                onConfirm = confirmClick,
                title = title,
                description = description,
                cancelText = "Dismiss"
            )
        }

    }


}


@Composable
private fun PlayerActionStatus(
    isDownloaded:Boolean = false,
    isPaused:Boolean = false,
    downloadProgress:Float= 0F,
    showProgress:Boolean = false,
    onIconClick:() -> Unit,
){
    val topIcon = when {
        isDownloaded -> {
            com.crezent.design_system.R.drawable.baseline_delete_24
        }

        isPaused -> {
            com.crezent.design_system.R.drawable.pause

        }

        else -> {
            com.crezent.design_system.R.drawable.download

        }
    }
    Box(
        modifier = Modifier
            .size(50.dp),
        contentAlignment = Alignment.Center
    ){

        if(showProgress){
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(),
                progress = downloadProgress,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer,
                strokeWidth = 1.dp

            )
        }
        Icon(
            modifier = Modifier.clickable {
                onIconClick()
            },
            painter = painterResource(id = topIcon),
            contentDescription =  "Back",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )

    }



}








@Preview
@Composable
private fun Preview(){
    ExMusicTheme()
    {
        var  progress = 0.1f
        val currentSong = Song(
            songId = "hdjksdmddi",
            title = "LOKE LOKE",
            artistUsername =    "SINXZE",
            description = "Descrption",
            date = "",
            length = 2.0,
            audioUrl = ""
        )
        val state = PlayerScreenState( totalDuration = 2000, currentDuration = 500 )
        val snackbarHostState = remember {
            SnackbarHostState()
        }
        val context = LocalContext.current
        val imageLoader = ImageLoader(context = context)
        PlayerScreen(
            navigateBack = { /*TODO*/ },
            downloadEvent = {} ,
            snackBarHostState = snackbarHostState,
            imageLoader =imageLoader ,
            route ="" ,
            onNavigate = {

            },
            musicAction =  {

            },
            state = state
        )
    }
}