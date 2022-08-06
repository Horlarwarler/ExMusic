package com.example.talimlectures.domain.download



sealed class DownloadOptions( success: Boolean = false,  message: String? = null){
    class StartDownload(success: Boolean, message: String):DownloadOptions(success = success , message = message)
    class FinishDownload(success: Boolean, message: String):DownloadOptions(success = success , message = message)
}