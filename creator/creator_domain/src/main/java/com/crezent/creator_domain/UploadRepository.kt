package com.crezent.creator_domain

import com.crezent.common.util.RequestResult
import kotlinx.coroutines.flow.Flow

interface UploadRepository {
    fun uploadSong(
        title:String,
        description:String,
        length: Double,
        songByteArray:ByteArray,
        thumbnailByteArray:ByteArray?
    ):Flow<RequestResult<Unit>>

    fun updateSong(
        songId: String,
        title:String?,
        description:String?,
        length: Double?,
        songByteArray:ByteArray?,
        thumbnailByteArray:ByteArray?
    ):Flow<RequestResult<Unit>>

    fun deleteSong(
        songId: String
    ):Flow<RequestResult<Unit>>


    fun addToPlaylist(
        songId: String
    ): Flow<RequestResult<Unit>>

    fun getAllPlaylist(

    ):Flow<RequestResult<Unit>>
    fun deletePlaylist(
        playlistId: String
    ):Flow<RequestResult<Unit>>
}