package com.crezent.creator_presentation.creator_upload

import java.io.File

data class CreatorUploadState(
    val song: com.crezent.models.Song? = null,
    val title:String? =null,
    val description: String? = null,
    val file: File?  = null,
    val fileIsValid:Boolean = false,
    val serverIsLoading:Boolean = false,
    val lectureIsUploaded:Boolean = false,
    val isLoading:Boolean = false,
   // val serverResponseMessage: ServerResponseMessage? = null,
    val errorMessages:List<String> = emptyList()
)
