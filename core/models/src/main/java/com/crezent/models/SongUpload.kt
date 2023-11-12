package com.crezent.models

import kotlinx.serialization.Serializable

@Serializable
data class SongUpload(
    val songId:String? = null,
    val tempSongId:String? = null, //WHEN Uploading, we don't know the song id
    val title:String,
    val status: TaskStatus = TaskStatus.NONE,
    val progress:Int = 0,
    val errorMessage:String? = null,
    val thumbnailUriString:String? = null,
    val songUriString:String? = null,
    val uploadDate:String,
    val elapsedTime:String = "0:00",
)
