package com.crezent.models

import kotlinx.serialization.Serializable

typealias SerializedMediaItem = MediaItem
@Serializable
data class MediaItem(
    val title:String? = null,
    val description:String? = null,
    val songId:String? = null,
    val thumbnail: String? = null,
    val artist:String? = null,
    val audioLink:String? = null,
    val date:String? = null,
    val length:Double = 0.0,
    val isDownloaded:Boolean = false

)