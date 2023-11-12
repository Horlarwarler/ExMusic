package com.crezent.models


data class User(
    val username:String,
    val displayName:String,
    val emailAddress:String,
    val password:String?,
    val profilePicture:String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val favSongs: List<String> = emptyList(),
    val recentlyPlayed:List<String> = emptyList(),
    val registeredDate:String,
    val isArtist:Boolean = false

)

