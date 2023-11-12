package com.crezent.network.models

import com.crezent.database.model.SongEntity
import kotlinx.serialization.Serializable

@Serializable
data class SongDto(
    val songId:String? = null,
    val title:String? = null,
    val description:String? = null,
    val artistUsername: String ?=  null,
    val thumbnail:String? = null,
    val length: Double?= null,
    val url:String? = null,
    val createdDate: String? = null,
    )
