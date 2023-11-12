package com.crezent.models

data class ArtistPlaylist(
    val artistUsername:String,
    val playlistName:String,
    val songs:String = "",
    val playlistId:String,
    val thumbnail:String? =null
)
