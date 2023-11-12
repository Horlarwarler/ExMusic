package com.crezent.user_presentation.profile.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DownloadDone
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.crezent.design_system.ExMusicIcons
import com.crezent.models.DownloadAction
import com.crezent.models.PersonalPlaylist
import com.crezent.models.TaskStatus

@Composable
fun PlaylistCard(
    playlist: PersonalPlaylist,
    imageLoader: ImageLoader,
    event : (DownloadAction) -> Unit
){
    val painter = rememberAsyncImagePainter(
        model =playlist.thumbnail,
        placeholder = painterResource(id =  com.crezent.design_system.R.drawable.small_girl),
        error = painterResource(id =  com.crezent.design_system.R.drawable.small_girl),
        imageLoader = imageLoader

    )
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .size(70.dp),
            painter =painter,
            contentDescription = "Thumbnail",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
            ,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 3.dp),
                text = playlist.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier.padding(vertical = 3.dp),
                text = playlist.artistName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,


                )
        }

        when {


            playlist.taskStatus == TaskStatus.DONE || playlist.isDownloaded  -> {
                //Show Dialog
                Icon(
                    imageVector = Icons.Rounded.DownloadDone,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Download"
                )
            }

            playlist.taskStatus == TaskStatus.NONE -> {
                Icon(
                    imageVector = ExMusicIcons.downloadForOffline,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Download",
                    modifier = Modifier.clickable {
                        event(DownloadAction.DOWNLOAD)
                    }
                )
            }

            playlist.taskStatus == TaskStatus.ONGOING -> {
                val progressValue = playlist.downloadProgress.toFloat() / 100
                val progress by animateFloatAsState(
                    targetValue = progressValue / 100,
                    label = "Animate Status",
                    animationSpec = InfiniteRepeatableSpec(
                        animation = TweenSpec(
                            durationMillis = 1000,
                            easing = LinearOutSlowInEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                )
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                           event(DownloadAction.PAUSE_DOWNLOAD)
                        }
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        strokeWidth = 0.5.dp
                    )
                    Icon(
                        imageVector =ExMusicIcons.downloading,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Download",
                    )
                }

            }

            playlist.taskStatus == TaskStatus.ERROR -> {
                Icon(
                    modifier = Modifier.clickable {
                          event(DownloadAction.RETRY_DOWNLOAD)
                    },
                    imageVector =ExMusicIcons.errorOutline,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Download"
                )
            }
        }
    }
}