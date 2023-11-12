package com.crezent.network.api


import com.crezent.common.util.RequestResult
import com.crezent.models.TempDownload
import com.crezent.network.models.ArtistPlaylistDto
import com.crezent.network.models.SongDto
import com.crezent.network.models.UserDto
import com.crezent.network.util.RealtimeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface BaseApi {

    val songs: StateFlow<List<SongDto>>

    val followings: StateFlow<List<UserDto>>

    val followers: StateFlow<List<UserDto>>

    val artists:StateFlow<List<UserDto>>

    suspend fun signUp(
        username: String,
        password: String,
        displayName:String,
        emailAddress:String,
        profilePicture: ByteArray?,
        asArtist:String
    ) : RequestResult<UserDto>

    suspend fun signIn(
        username: String,
        password: String
    ): RequestResult<String>

    suspend fun updateProfile(
        username: String?,
        password: String?,
        displayName:String?,
        emailAddress:String?,
        asArtist: Boolean? = null,
        profilePicture: ByteArray?,
        profilePictureRemove:String
        ) : RequestResult<UserDto>
    suspend fun connect()

    suspend fun disconnect(): RequestResult<String>

    suspend fun sendAction(action:RealtimeAction): RequestResult<Unit>



    fun authenticate(): Flow<RequestResult<Unit>>

    suspend fun deleteAccount(password:String): RequestResult<Unit>

    suspend fun getUserInfo(username:String? = null): RequestResult<UserDto>

    suspend fun getSongs(
        searchQuery:String = "",
        artistUsername:String? = null,
        )
    : RequestResult<List<SongDto>>

    suspend fun getSongById(songId:String) : RequestResult<SongDto>

    suspend fun getArtistPlaylist(artistUsername:String? = null): RequestResult<List<ArtistPlaylistDto>>

    suspend fun getPlaylistById(playlistId:String) : RequestResult<ArtistPlaylistDto>

    suspend fun downloadAudio(
        songId:String,
        audioName: String,
        imageName:String? = null,
        tempFileChange:(File,File?) -> Unit,
        tempDownload: TempDownload
    ):Flow<RequestResult<Pair<ByteArray, ByteArray?>>>

    companion object {
        const val baseUrl = "http://0.0.0.0:8080/"
        const val songAudio="song-audio"
        const val downloadAudio="download-audio"
        const val downloadThumbnail="download-thumbnail"
        const val songThumbnail = "song-thumbnail"
        const val signUp = "sign-up"
        const val signIn= "sign-in"

        const val authenticate ="authenticate"

        const val editProfile ="edit-profile"
        const val deleteAccount ="delete-account"
        const val user ="user"

        const val followings = "followings"
        const val followers = "followers"
        const val artist = "artist"
        const val follow ="follow"
        const val unFollow = "unfollow"


        const val playlist = "playlist"
        const val createPlaylist = "create-playlist"
        const val deletePlaylist = "delete-playlist"
        const val addToPlaylist = "add-playlist"
        const val removeFromPlaylist = "remove-playlist"

        const val song ="song"
        const val editSong = "edit-song"
        const val uploadSong= "upload-song"
        const val deleteSong= "delete-song"

    }




}