package com.crezent.network.models

import com.crezent.database.model.ArtistPlaylistEntity
import com.crezent.models.ArtistPlaylist
import kotlinx.serialization.Serializable

@Serializable
data class ArtistPlaylistDto(
    val artistUsername:String,
    val playlistName:String,
    val songs:List<String> = emptyList(),
    val playlistId:String,
    val thumbnail:String? = null
) {
   fun toArtistPlaylist(): ArtistPlaylist {
       return ArtistPlaylist(
           artistUsername = artistUsername,
           playlistName = playlistName,
           songs = "emptyList<>()",
           playlistId = playlistId,
           thumbnail = thumbnail
       )
   }
    fun toEntity():ArtistPlaylistEntity {
        //TODO
        return ArtistPlaylistEntity(
            artistUsername = artistUsername,
            playlistName = playlistName,
            songs = "",
            playlistId = playlistId,
            thumbnail = thumbnail
        )
    }
}
