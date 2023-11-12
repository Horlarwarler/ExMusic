package com.crezent.user_presentation.homeScreen

import com.crezent.models.RecentlyPlayed
import com.crezent.models.Song
import com.crezent.models.User

data class HomeScreenState(
    val recentlyPlayed: List<RecentlyPlayed> = emptyList(),
    val  recentIsIsLoading:Boolean = false,
    val playingMusic:String? = null,
    val loggedInUser: User? = null,
    val  userIsLoading:Boolean = false,
    val followedArtist:List<User> = emptyList(),
    val recommendedArtist:List<User> = emptyList(),
    val  artistIsLoading:Boolean = false,
    val trendingSongs : List<Song> = emptyList(),
    val  playlistIsLoading:Boolean = false,
    val errors: List<String> = emptyList(),
    )
