package com.crezent.creator_presentation.creator_upload


sealed class CreatorUploadEvent {
   // class AddToProgress(val upload:CurrentUpload): CreatorUploadEvent()
    class RemoveFromProgress(val songId:String): CreatorUploadEvent()

    object RemoveShownMessage: CreatorUploadEvent()
}