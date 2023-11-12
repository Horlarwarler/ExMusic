package com.crezent.download

sealed class DownloadEvent() {

    class StartDownload(val songId:String): DownloadEvent()
    class StopDownload(val songId:String): DownloadEvent()
    class PauseDownload(val songId:String): DownloadEvent()
    class RetryDownload(val songId:String, val shouldRestart:Boolean = false): DownloadEvent()
    class ResumeDownload(val songId:String): DownloadEvent()
    class RemoveFromDownload(val songId:String): DownloadEvent()
}