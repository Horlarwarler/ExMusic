package com.crezent.data.mapper

import com.crezent.database.model.PlaylistEntity
import com.crezent.models.ArtistPlaylist
import com.crezent.models.PersonalPlaylist
import com.crezent.network.models.ArtistPlaylistDto

fun ArtistPlaylist.toDto(): ArtistPlaylistDto {

    //TODO
    return ArtistPlaylistDto(
        artistUsername = artistUsername,
        playlistName = playlistName,
        songs = emptyList(),
        playlistId = playlistId
    )
}

fun ArtistPlaylistDto.toPlaylist(): ArtistPlaylist {

    return ArtistPlaylist(
        artistUsername = artistUsername,
        playlistName = playlistName,
        songs = "TODO",
        playlistId = playlistId,
        thumbnail = thumbnail
    )
}



fun PersonalPlaylist.toEntity(): PlaylistEntity {
    return  PlaylistEntity(
        songId = songId,
        name = name,
        thumbnail = thumbnail,
        artistName = artistName,
        songUrl = songUrl
    )
}

