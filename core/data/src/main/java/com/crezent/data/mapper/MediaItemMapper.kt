package com.crezent.data.mapper

import androidx.media3.common.MediaItem
import com.crezent.models.SerializedMediaItem

fun MediaItem.mapToSerialized(): SerializedMediaItem {
    val title = mediaMetadata.title.toString()
    val description = mediaMetadata.description.toString()
    val thumbnail = mediaMetadata.extras?.getString("thumbnail")
    val audioUrl = mediaMetadata.extras?.getString("audioUrl")
    val date = mediaMetadata.extras?.getString("date")
    val songId = mediaId
    val artist = mediaMetadata.artist.toString()
    return SerializedMediaItem(
        title = title,
        description = description,
        thumbnail = thumbnail,
        songId = songId,
        artist = artist,
        audioLink = audioUrl,
        date = date,
    )
}