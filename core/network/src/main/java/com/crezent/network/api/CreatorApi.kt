package com.crezent.network.api


import com.crezent.common.util.RequestResult
import com.crezent.models.ArtistPlaylist
import kotlinx.coroutines.flow.Flow

interface CreatorApi {
    fun uploadSong(
        title:String,
        description:String,
        length: Double,
        songByteArray:ByteArray,
        thumbnailByteArray:ByteArray?
    ) :Flow<RequestResult<Unit>>
    fun updateSong(
        songId: String,
        title:String?,
        description:String?,
        length: Double?,
        songByteArray:ByteArray?,
        thumbnailByteArray:ByteArray?) :Flow<RequestResult<Unit>>

    fun deleteSong(songId:String) : Flow< RequestResult<Unit>>


    fun deletePlaylist(playlistId:String) :Flow< RequestResult<Unit>>

    fun addToPlaylist(songId:String,) :Flow< RequestResult<Unit>>
    fun createPlaylist(playlistName: String): Flow< RequestResult<ArtistPlaylist>>


    //suspend fun c

    companion object {

        const val createPlaylist = "create-playlist"
        const val deletePlaylist = "delete-playlist"
        const val addToPlaylist = "add-playlist"
        const val removeFromPlaylist = "remove-playlist"

        const val editSong = "edit-song"
        const val uploadSong= "upload-song"
        const val deleteSong= "delete-song"

    }

}