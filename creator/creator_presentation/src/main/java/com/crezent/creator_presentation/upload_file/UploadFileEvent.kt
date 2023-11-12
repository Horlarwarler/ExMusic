package com.crezent.creator_presentation.upload_file

import android.net.Uri

sealed class UploadFileEvent {
    class EditSong(val songUri: Uri?): UploadFileEvent()
    class EditThumbnail(val thumbnailUri: Uri?): UploadFileEvent()
    class EditTitle(val title:String) : UploadFileEvent()
    class EditDescription(val description:String) : UploadFileEvent()
    
    object RemoveShownMessage: UploadFileEvent()
    
    object Upload : UploadFileEvent()

    object OnStart : UploadFileEvent()
    
    
}