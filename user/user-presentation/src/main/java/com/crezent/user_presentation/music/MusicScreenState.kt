package com.crezent.user_presentation.music

import com.crezent.models.ArtistPlaylist
import com.crezent.models.Song
import com.crezent.models.User

data class MusicScreenState(
    val songs:List<Song> = emptyList(),
    val songIsLoading:Boolean = false,
    val playlists: List<ArtistPlaylist> = emptyList(),
    val playlistIsLoading:Boolean = false,
    val artistIsLoading:Boolean = false,
    val artists: List<User> = emptyList(),
    val isAuthenticated:Boolean = false,
    val isLoading:Boolean = false,
    val errors:List<String> = emptyList(),
    val actionLoading:Boolean = false,
    val searchQuery:String = ""
)
