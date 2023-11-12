package com.crezent.data.repository

import com.crezent.common.util.RequestResult
import kotlinx.coroutines.flow.Flow

interface ExMusicRepo {
    fun getSongs(
        searchQuery:String = "",
        artistUsername:String? = null,
    ):Flow<RequestResult<List<com.crezent.models.Song>>>

    suspend fun getSongById(
        songId:String
    ): com.crezent.models.Song?

    suspend fun deleteDownloadedSong(
        songId: String
    ): RequestResult<Unit>

    fun getUser(
        username:String? = null,
        cacheUser:Boolean = false
    ):Flow <RequestResult<com.crezent.models.User>>

    suspend fun insertUser(
        user: com.crezent.models.User
    )



    //user can follows many users
    //a user can be followed by many users
    suspend fun getPersonalPlaylistById(songId: String): com.crezent.models.PersonalPlaylist?

    suspend fun addToPersonalPlaylist(personalPlaylist: com.crezent.models.PersonalPlaylist): RequestResult<Unit>

    suspend fun removeFromPersonalPlaylist(songId: String): RequestResult<Unit>

    suspend fun updatePersonalPlaylist(personalPlaylist: com.crezent.models.PersonalPlaylist): RequestResult<Unit>


    fun getPersonalPlaylist() : Flow<RequestResult<List<com.crezent.models.PersonalPlaylist>>>
    fun getArtistPlaylist(artistUsername: String?) : Flow<RequestResult<List<com.crezent.models.ArtistPlaylist>>>
    fun getArtists() : Flow<RequestResult<List<com.crezent.models.User>>>

    //Recently Played

    suspend fun addToRecentlyPlayed(recentlyPlayed: com.crezent.models.RecentlyPlayed)

    suspend fun removeFromRecent(songId: String)

    fun getAllRecentlyPlayed():Flow<RequestResult<List<com.crezent.models.RecentlyPlayed>>>

}