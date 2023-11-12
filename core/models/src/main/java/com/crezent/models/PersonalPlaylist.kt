package com.crezent.models


data class PersonalPlaylist(
    val songId:String,
    val name:String,
    val thumbnail:String?,
    val artistName:String,
    val taskStatus: TaskStatus = TaskStatus.NONE,
    val downloadProgress:Int = 0,
    val songUrl:String,
    val isDownloaded:Boolean = false
)
