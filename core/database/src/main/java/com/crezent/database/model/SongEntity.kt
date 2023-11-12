package com.crezent.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crezent.models.Song

@Entity(tableName = "SONG_TABLE")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int? =null,
    val songId:String,
    val title:String,
    val description:String,
    val artistUsername: String,
    val thumbnail:String? = null,
    val length: Double,
    val audioLink:String,
    val createdDate: String,
    val isDownloaded:Int = 0
)

fun SongEntity.toSong(): Song {

    return Song(
        songId = songId,
        title = title,
        description = description,
        artistUsername = artistUsername,
        thumbnailUrl = thumbnail,
        length = length,
        audioUrl = audioLink,
        date = createdDate,
        isDownloaded = isDownloaded == 1

    )
}

