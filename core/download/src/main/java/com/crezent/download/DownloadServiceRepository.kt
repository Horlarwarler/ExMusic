package com.crezent.download

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.crezent.models.FileResult
import com.crezent.models.MediaItem
import com.crezent.download.DownloadService.Companion.CURRENT_DOWNLOADS
import com.crezent.download.DownloadService.Companion.DOWNLOAD_RESULT
import com.crezent.download.DownloadService.Companion.DOWNLOAD_STATUS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DownloadServiceRepository (private val context: Context) {
    private val _currentDownloadResult : MutableStateFlow<com.crezent.models.FileResult?> = MutableStateFlow(null)
    val currentDownloadResult  = _currentDownloadResult.asStateFlow()

    private val _currentDownloads : MutableStateFlow<List<com.crezent.models.FileResult>> =
        MutableStateFlow(emptyList())
     val currentDownloads = _currentDownloads.asStateFlow()
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent?.action == DOWNLOAD_STATUS){
                val currentDownloadsString = intent.getStringExtra(CURRENT_DOWNLOADS)
                currentDownloadsString?.let {
                    val currentDownloadDecoded = Json.decodeFromString<List<com.crezent.models.FileResult>>(currentDownloadsString!!)
                    _currentDownloads.update {
                        currentDownloadDecoded
                    }
                }

                val downloadResultString = intent.getStringExtra(DOWNLOAD_RESULT)
                downloadResultString?.let {

                    val downloadResultDecoded = Json.decodeFromString<com.crezent.models.FileResult>(downloadResultString)
                    _currentDownloadResult.update {
                      downloadResultDecoded
                    }
                }
            }
        }
    }
    init {
        registerBroadcast()
    }

    fun startDownload(mediaItem: com.crezent.models.MediaItem, duration:Double){
        val playingMediaItem: com.crezent.models.MediaItem = mediaItem
        val  intent = Intent(context, DownloadService::class.java)
            .apply {
                putExtra(com.crezent.common.util.Constant.SONG_ID,playingMediaItem.songId)
                putExtra(DownloadService.AUDIO_URL, playingMediaItem.audioLink)
                putExtra(DownloadService.CREATED_DATE, playingMediaItem.date)
                putExtra(DownloadService.THUMBNAIL_URL, playingMediaItem.thumbnail)
                putExtra(DownloadService.ARTIST_USERNAME, playingMediaItem.artist)
                putExtra(DownloadService.SONG_DESCRIPTION, playingMediaItem.description)
                putExtra(DownloadService.SONG_LENGTH, duration)
                putExtra(DownloadService.SONG_TITLE, playingMediaItem.title)
                putExtra(DownloadService.DOWNLOAD_ACTION, DownloadService.START_DOWNLOAD)
            }
        context.startService(intent)

    }
    fun stopDownload(songId:String){
        val intent = Intent(context, DownloadService::class.java)
            .apply {
                putExtra(com.crezent.common.util.Constant.SONG_ID,songId)

                putExtra(DownloadService.DOWNLOAD_ACTION, DownloadService.START_DOWNLOAD)
            }
        context.startService(intent)

    }

    fun pauseDownload(songId:String){
        val intent = Intent(context, DownloadService::class.java)
            .apply {
                putExtra(com.crezent.common.util.Constant.SONG_ID,songId)

                putExtra(DownloadService.DOWNLOAD_ACTION, DownloadService.PAUSE_DOWNLOAD)
            }
        context.startService(intent)

    }
    fun resumeDownload(songId: String){
        val intent = Intent(context, DownloadService::class.java)
            .apply {
                putExtra(com.crezent.common.util.Constant.SONG_ID,songId)

                putExtra(DownloadService.DOWNLOAD_ACTION, DownloadService.RESUME_DOWNLOAD)
            }
        context.startService(intent)

    }

    fun retryDownload(songId: String, shouldRestart:Boolean = false ){
        val intent = Intent(context, DownloadService::class.java)
            .apply {
                putExtra(com.crezent.common.util.Constant.SONG_ID,songId)

                putExtra(DownloadService.DOWNLOAD_ACTION, DownloadService.RETRY_DOWNLOAD)
                putExtra(DownloadService.SHOULD_RESTART, shouldRestart)

            }
        context.startService(intent)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerBroadcast(){
        val intentFilter = IntentFilter(DOWNLOAD_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(broadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
        }
        else {
            context.registerReceiver(broadcastReceiver, intentFilter)
        }
    }

    private fun unregisterReceiver(){
        context.unregisterReceiver(broadcastReceiver)
    }
}