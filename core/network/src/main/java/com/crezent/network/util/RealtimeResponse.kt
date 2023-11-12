package com.crezent.network.util

import com.crezent.network.models.SongDto
import com.crezent.network.models.UserDto

sealed  interface RealtimeResponse{
    data class Songs(val songs:List<SongDto>) :RealtimeResponse
    data class Followers(val followers: List<UserDto>) :RealtimeResponse
    data class Followings(val followings:List<UserDto>) :RealtimeResponse
}