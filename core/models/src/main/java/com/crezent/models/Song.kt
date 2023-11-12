package com.crezent.models


data class Song(
    val songId:String,
    val title:String,
    val description:String,
    val artistUsername: String,
    val thumbnailUrl:String? = null,
    val length: Double,
    val audioUrl:String,
    val date: String,
    val isDownloaded:Boolean = false
    )

