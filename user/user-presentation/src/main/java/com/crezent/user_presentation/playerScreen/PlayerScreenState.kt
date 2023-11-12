package com.crezent.user_presentation.playerScreen

import androidx.media3.common.Player

data class PlayerScreenState(
    val favouriteEntities:List<String> = emptyList(),
    val currentDuration:Long = 0,
    val totalDuration:Long = 0,
    val repeatMode:Int = Player.REPEAT_MODE_OFF,
    val isShuffle:Boolean = false,
    @Player.State val playerState: Int = Player.STATE_IDLE,
    val isPlaying:Boolean = false,
    val playingMediaItem: com.crezent.models.MediaItem? = null,
    val isDownloaded:Boolean = false,
    val currentDownload: com.crezent.models.FileResult? = null,
)
