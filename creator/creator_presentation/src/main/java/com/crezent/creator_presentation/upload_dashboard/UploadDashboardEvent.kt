package com.crezent.creator_presentation.upload_dashboard

import android.net.Uri


sealed class UploadDashboardEvent {

    class SelectAudioFile (val selectedUri: Uri) : UploadDashboardEvent ()
    class  SelectThumbnailFile (val selectedUri: Uri): UploadDashboardEvent()

    object  RemoveThumbnailFile : UploadDashboardEvent()

    object  PreviewSelectedAudio : UploadDashboardEvent()
    object  RemoveShownMessage : UploadDashboardEvent()

    class TitleChange (val title:String) : UploadDashboardEvent()
    class DescriptionChange (val description:String) : UploadDashboardEvent()

}