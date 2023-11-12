package com.crezent.network.util

import com.crezent.network.models.SongDto
import com.crezent.network.models.UserDto
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun  String.toRealTimeResponse(): RealtimeResponse{
    //#followers:List<User>

    val substringBefore = substringBefore(":")
    val substringAfter = substringAfter(":")

    return when(substringBefore){
        "#followers" -> {
            val followers = Json.decodeFromString<List<UserDto>>(substringAfter)
            RealtimeResponse.Followers(followers)
        }
        "#followings" -> {
            val followings = Json.decodeFromString<List<UserDto>>(substringAfter)
            RealtimeResponse.Followers(followings)
        }
        else -> {
            val songs = Json.decodeFromString<List<SongDto>>(substringAfter)
            RealtimeResponse.Songs(songs)
        }
    }
}