package com.crezent.network.models

import kotlinx.serialization.Serializable


@Serializable
data class BasicUserInfoDto(
    val username:String,
    val displayName:String,
    val profilePicture:String? = null,
    val isArtist:Boolean = false,
)
