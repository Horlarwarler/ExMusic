package com.crezent.data.mapper

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.crezent.models.Song
import com.crezent.database.model.SongEntity
import com.crezent.network.models.SongDto

fun Song.mapToDto(): SongDto {

    return SongDto(
        songId = songId,
        title = title,
        description = description,
        artistUsername = artistUsername,
        thumbnail = thumbnailUrl,
        length = length,
        createdDate = date
    )
}

fun SongDto.toSong(): Song {

    return Song(
        songId = songId!!,
        title = title!!,
        description = description!!,
        artistUsername = artistUsername!!,
        thumbnailUrl = thumbnail,
        length = length!!,
        date = createdDate!!,
        audioUrl = url!!,
        isDownloaded = false
    )
}

fun SongDto.toEntity():SongEntity{
    return  SongEntity(
        songId = songId!!,
        title = title!!,
        description = description!!,
        artistUsername = artistUsername!!,
        thumbnail = thumbnail,
        length = length!!,
        audioLink = url!!,
        createdDate = createdDate!!,
        isDownloaded = 0
    )
}



fun Song.toEntity(): SongEntity {

    return  SongEntity(
        songId = songId,
        title = title,
        description = description,
        artistUsername = artistUsername,
        thumbnail = thumbnailUrl,
        length = length,
        audioLink = audioUrl,
        createdDate = date,
        isDownloaded = 0
    )
}


 fun Song.mapToMediaItem(): MediaItem {
     val extras = Bundle()
    val mediaMetadata = MediaMetadata.Builder()
        .setTitle(title)
        .setDescription(description)
        .setArtist(artistUsername)
        .apply {
            thumbnailUrl?.let {
                extras.putString("thumbnail", thumbnailUrl)
            }
            extras.putString("audioUrl", audioUrl)
            extras.putString("date", date)
            setExtras(extras)
        }
        .build()
    val mediaItem = MediaItem.Builder()
        .setMediaMetadata(mediaMetadata)
        .apply {
            if (isDownloaded){
                setUri(Uri.parse(audioUrl))
            }
            else{
                setUri(audioUrl)
            }
        }
        .setMediaId(songId)
        .build()
    return  mediaItem
}


