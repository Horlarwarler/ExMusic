package com.crezent.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crezent.models.ArtistPlaylist
import com.crezent.models.Song
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Entity("artist_playlist")
data class ArtistPlaylistEntity(
    @PrimaryKey()
    val playlistId:String,
    val artistUsername:String,
    val playlistName:String,
    val songs:String ,
    val thumbnail:String? =null
)

fun ArtistPlaylistEntity.toArtistPlaylist(): ArtistPlaylist {

    return ArtistPlaylist(
        artistUsername = artistUsername,
        playlistName = playlistName,
        songs = songs,
        playlistId = playlistId,
        thumbnail = thumbnail
    )
}


