package com.crezent.creator_presentation.upload_file

import android.net.Uri
import com.crezent.common.util.LinkedList
import com.crezent.models.SongUpload
import com.crezent.models.User

data class UploadFileState(
    val user: User? = null,
    val uploadingFile: SongUpload? = null,
    val songUri:Uri? = null,
    val thumbnailUri: Uri? = null,
    val error: LinkedList.Node<String>? = null,
    val isLoading:Boolean = false,
    val title:String? = null,
    val description:String? = null,
    val uploadButtonEnabled:Boolean = false,
    val selectedFileName:String? = null,
    val songName:String? = null,
    val thumbnailName:String? = null,
    val duration:Double? = null,
    val songFileSize:Double? = null,
    val thumbnailFileSize:Double? = null

)
