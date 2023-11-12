package com.crezent.music

import androidx.media3.common.Player
import com.crezent.models.MediaItem
import kotlinx.serialization.Serializable

@Serializable
data class PlaybackState(
    val mediaItem: MediaItem? = null,
    val currentPosition:Long = 0,
    val contentDuration:Long = 0,
    val repeatMode:Int = Player.REPEAT_MODE_OFF,
    val shuffle:Boolean = false,
    val playerState: Int = Player.STATE_IDLE,
    val isPlaying:Boolean = false,

    )
