package com.crezent.creator_domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrentUpload(
    val songId:String,
    val title:String,
    val uriString:String? = null,
    val uploadDate:String,
    val elapsedTime:String = "0:00",

)
