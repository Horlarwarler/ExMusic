package com.crezent.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crezent.models.PersonalPlaylist


@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey
    val songId:String,
    val name:String,
    val thumbnail:String?,
    val artistName:String,
    val songUrl:String,
    val isDownloaded:Int = 0
)

fun PlaylistEntity.toPersonalPlaylist(): PersonalPlaylist {
    return PersonalPlaylist(
        songId = songId,
        name = name,
        thumbnail = thumbnail,
        artistName = artistName,
        songUrl = songUrl

    )
}