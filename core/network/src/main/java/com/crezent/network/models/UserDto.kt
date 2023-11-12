package com.crezent.network.models

import com.crezent.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username:String,
    val displayName:String? = null,
    val emailAddress:String? = null,
    val password:String? = null,
    val profilePicture:String? = null,
    val followers: List<String> = emptyList(),
    val following: List<String> = emptyList(),
    val favSongs: List<String> = emptyList(),
    val recentlyPlayed:List<String> = emptyList(),
    val isArtist:Boolean = false,
    val registeredDate:String? = null,
    ){
    fun toUser(): User {
        return  User(
            username = username,
            displayName = displayName!!,
            password = password,
            emailAddress = emailAddress!!,
            profilePicture = profilePicture,
            isArtist = isArtist?:false,
            registeredDate = registeredDate!!,
            following = following,
            followers = followers,
            favSongs = favSongs
        )
    }
}




