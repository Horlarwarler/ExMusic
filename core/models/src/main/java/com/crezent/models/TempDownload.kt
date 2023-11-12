package com.crezent.models

import kotlinx.coroutines.Job
import java.io.File
import java.util.Calendar

data class TempDownload(
    val song: Song,
    val tempAudioFile: File? = null,
    val tempThumbnailFile: File? = null,
    val downloadStatus: DownloadStatus = DownloadStatus.IDLE,
    val tempJob: Job? = null,
    val notificationId:Int = generateTimeStamp()
){
    companion object {
        fun generateTimeStamp():Int {
            val calendar = Calendar.getInstance()
            return calendar.timeInMillis.toInt()
        }
    }
}
