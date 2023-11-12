package com.crezent.models



data class BasicUserInfo(
    val username:String,
    val displayName:String,
    val profilePicture:String? = null,
    val isArtist:Boolean = false
)
