package com.crezent.download

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.crezent.common.util.Constant.SONG_ID
import com.crezent.common.util.RequestResult
import com.crezent.common.util.generateRandomString
import com.crezent.models.FileResult
import com.crezent.models.Song
import com.crezent.network.api.BaseApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class DownloadService  : Service() {

    //private val jobsHashmap: HashMap<String, DownloadJob> = hashMapOf()
    private val downloadStatusHashmap: HashMap<String, FileResult> = hashMapOf()
    private val tempDownloads:HashMap<String, com.crezent.models.TempDownload> = hashMapOf()
   // @Inject
 //   lateinit var userApi: UserApi
    @Inject
    lateinit var baseApi: BaseApi

  //  @Inject
   /// lateinit var localMusic: LocalMusic


//    private val downloadNotification by lazy {
//        DownloadNotification(this)
//    }

    private val fileHelper by lazy {
        com.crezent.common.util.FileHelper(this)
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val downloadAction = intent?.getStringExtra(DOWNLOAD_ACTION)
        val songId = intent!!.getStringExtra(SONG_ID)!!
        val audioUrl = intent.getStringExtra(AUDIO_URL)
        val songTitle = intent.getStringExtra(SONG_TITLE)
        val shouldRestart = intent.getBooleanExtra(SHOULD_RESTART, false)
        val thumbnailUrl:String? = intent.getStringExtra(THUMBNAIL_URL)
        val artist:String? = intent.getStringExtra(ARTIST_USERNAME)
        val length:Double =intent.getDoubleExtra(SONG_LENGTH,0.0)
        val description = intent.getStringExtra(SONG_DESCRIPTION)
        val date = intent.getStringExtra(CREATED_DATE)


        when(downloadAction){

            START_DOWNLOAD ->{
                val song = Song(
                    songId = songId,
                    title = songTitle!!,
                    audioUrl = audioUrl!!,
                    artistUsername = artist!!,
                    thumbnailUrl = thumbnailUrl,
                    length = length,
                    description = description!!,
                    date = date!!
                )
                startDownload(song = song)
            }
            PAUSE_DOWNLOAD ->{
                pauseDownload(songId)
            }
            STOP_DOWNLOAD ->{
                stopDownload(songId)
            }
           RESUME_DOWNLOAD -> {
               resumeDownload(songId)
           }
            RETRY_DOWNLOAD -> {
                retryDownload(songId = songId,shouldRestart = shouldRestart)
            }


            REMOVE_FROM_QUEUE ->{
                removeFromQueue(songId,)
            }

            GET_DOWNLOADS ->{
                sendCurrentDownloads()
            }

        }


        return super.onStartCommand(intent, flags, startId)
    }



    private fun startDownload(song: Song){

        val tempDownload = tempDownloads[song.songId]?:run {
            val tempThumbnailFile =song.thumbnailUrl?.let {
                File.createTempFile("tempImage", generateRandomString())
            }
            val newTempFile = com.crezent.models.TempDownload(
                song = song,
                tempAudioFile = File.createTempFile(
                    "tempAudio",
                    generateRandomString()
                ),
                tempThumbnailFile = tempThumbnailFile,
                downloadStatus = com.crezent.models.DownloadStatus.IDLE,
                tempJob = Job(),
            )
            tempDownloads[song.songId] = newTempFile
            newTempFile
        }

        val currentDownloadingFile = downloadStatusHashmap[song.songId]?:run {
            val newDownload = FileResult(
                title = song.title,
                songId = song.songId
            )
            downloadStatusHashmap[song.songId]= newDownload
            newDownload
        }
        if (currentDownloadingFile.taskStatus == com.crezent.models.TaskStatus.ONGOING){
            return
        }

        val title = song.title
        val audioUrl = song.audioUrl
        val notificationId = tempDownload.notificationId
        val thumbnailUrl = song.thumbnailUrl

        val audioName = audioUrl.substringAfter("http://10.42.0.1:8080/songs/")
        val imageName = thumbnailUrl?.substringAfter("http://10.42.0.1:8080/thumbnails/")

        val coroutineScope = CoroutineScope(Dispatchers.IO + tempDownload.tempJob!!)
        sendCurrentDownloads()
        coroutineScope.launch() {
            var showLoading = false
          baseApi.downloadAudio(
              songId = song.songId,
              audioName = audioName,
              imageName = imageName,
              tempDownload = tempDownload,
              tempFileChange = {
                  tempAudioFile, tempThumbnailFile ->
                  tempFileChange(song.songId, tempAudioFile, tempThumbnailFile)
              }
          ).collectLatest {
               requestResult->
              when(requestResult){

                  is RequestResult.Success->{
                     // downloadNotification.notificationManager.cancel(notificationId)

                      val arrayPair = requestResult.resource
                      val songArray = arrayPair.first
                      val thumbnailArray = arrayPair.second
                      Log.d(
                          "DOWNLOAD",
                          "Download Success song ${songArray.size} thumbnail ${thumbnailArray?.size}"
                      )

                      saveAudioToDevice(songArray = songArray, song = song, thumbnailArray = thumbnailArray)

                  }

                  is RequestResult.Loading ->{
                      val downloadRequest = FileResult(
                          title,
                          song.songId,
                          com.crezent.models.TaskStatus.ONGOING,
                          progress = requestResult.progress ?: 0
                      )
                      downloadStatusHashmap.replace(song.songId, downloadRequest)
                      val progress = requestResult.progress
                      val notificationTitle = "Downloading $title"
                      val notificationDescription = "$progress% out of 100%"
//                      val downloadingNotification = downloadNotification.buildNotification(
//                          title = notificationTitle,
//                          description = notificationDescription,
//                          channelId = DOWNLOAD_CHANNEL,
//                          icon = R.drawable.download,
//                          percent = progress?:0
//                      )
                      sendCurrentDownloads()

                    //  downloadNotification.notificationManager.notify(notificationId, downloadingNotification)
                  }
                  is RequestResult.Error ->{

                      val notificationTitle = "$title Failed To Download"
                      val notificationDescription = "Error: ${requestResult.errorMessage}"
                      //  downloadNotification.notificationManager.cancel(notificationId)
//                      sendNotification(
//                          notificationTitle = notificationTitle,
//                          notificationDescription = notificationDescription,
//                          notificationId = notificationId,
//                          autoCancel = true,
//                          icon =
//                      )
                      tempDownloads.remove(song.songId)

                      val downloadResult = FileResult(
                          title = title,
                          songId = song.songId,
                          taskStatus = com.crezent.models.TaskStatus.ERROR,
                          error = requestResult.errorMessage
                      )
                      downloadStatusHashmap.remove(song.songId)
                      sendFileResult(fileResult =downloadResult)
                      sendCurrentDownloads()
                  }
              }

           }

        }

    }

    private val tempFileChange : (songId:String, tempAudioFile: File, tempThumbnailFile: File?, ) -> Unit = {
        songId, tempAudioFile, tempThumbnailFile ->
        tempDownloads[songId]?.let {
                tempDownloads.replace(songId, it.copy(tempAudioFile =tempAudioFile, tempThumbnailFile = tempThumbnailFile ))
            }
    }

    private suspend fun saveAudioToDatabase(song: Song) {
      // localMusic.addToDownload(song)
    }

    private suspend fun saveAudioToDevice(songArray: ByteArray, song: Song, thumbnailArray: ByteArray? = null) {
        Log.d("DOWNLOAD", "1st step")

        val currentDownload = tempDownloads[song.songId]?:return
        var fileResult = downloadStatusHashmap[song.songId]?:return
        Log.d("DOWNLOAD", "2nd step")

        val title = song.title
        val notificationId = currentDownload.notificationId
        var notificationTitle = ""
        var notificationDescription = ""
      //  var icon = R.drawable.baseline_error_outline_24

        try {
           val songFileName = "${song.songId}.mp3"
            val songUri =  fileHelper.saveFile(songFileName, songArray)
           val thumbnailFileName = "${song.songId}.jpg"

           val thumbnailUri =  thumbnailArray?.let {
               fileHelper.saveFile(thumbnailFileName, thumbnailArray)
           }
           val songToSave = song.copy(
               audioUrl = songUri,
               thumbnailUrl = thumbnailUri
           )
           notificationTitle = "$title Downloaded"
           notificationDescription = "The Music item has been save locally to your device "
        //   icon = com.crezent.common.R.drawable.baseline_download_done_24
            saveAudioToDatabase(songToSave)
           fileResult = fileResult.copy(taskStatus = com.crezent.models.TaskStatus.DONE)
            Log.d("DOWNLOAD", "FIle save")



       }
       catch (error:FileAlreadyExistsException){
           notificationTitle = "$title Failed To Save"
           notificationDescription = "File Already Exists"
           fileResult = fileResult.copy(taskStatus = com.crezent.models.TaskStatus.ERROR)

       }
       catch (error: IOException){
           notificationTitle = "$title Failed To Save"
           notificationDescription = "Can't Write to your local device"
           fileResult = fileResult.copy(taskStatus = com.crezent.models.TaskStatus.ERROR)

       }
        catch (error:Exception){
            notificationTitle = "$title Failed To Save"
            notificationDescription = "Error $error'"
            fileResult = fileResult.copy(taskStatus = com.crezent.models.TaskStatus.ERROR)

        }
        finally {
            Log.d("DOWNLOAD", "Sent Notification")

          //  downloadNotification.notificationManager.cancel(notificationId)
//            sendNotification(
//                notificationTitle = notificationTitle,
//                notificationDescription = notificationDescription,
//                notificationId = notificationId,
//                icon = icon,
//                autoCancel = false
//            )
            Log.d("DOWNLOAD", "Sent Notification")
            downloadStatusHashmap.remove(song.songId)
            tempDownloads.remove(song.songId)
            sendCurrentDownloads()
            sendFileResult(fileResult = fileResult)

        }

    }

    private fun stopDownload(songId: String){
        val tempDownload = tempDownloads[songId]?:return
        tempDownload.tempJob?.cancel()
        tempDownloads.remove(songId)
        downloadStatusHashmap.remove(songId)
        sendCurrentDownloads()
    }

    private fun  pauseDownload(songId: String){
        val tempDownload = tempDownloads[songId]?:return

        val updateTempDownload = tempDownload.copy(downloadStatus = com.crezent.models.DownloadStatus.PAUSED, tempJob = null)
        tempDownloads.replace(songId, updateTempDownload)

        val currentDownload = downloadStatusHashmap[songId]?:return
//        downloadNotification.notificationManager.cancel(tempDownload.notificationId)
//        sendNotification(
//            notificationTitle = "Download Pause ${currentDownload.title}",
//            notificationDescription = "Downloading Pause, click to resume",
//            notificationId = tempDownload.notificationId,
//            icon = R.drawable.baseline_pause_24,
//            autoCancel = false
//        )
        downloadStatusHashmap.replace(songId, currentDownload.copy(taskStatus = com.crezent.models.TaskStatus.PAUSED))
        tempDownload.tempJob?.cancel()
        sendCurrentDownloads()
    }

    private fun resumeDownload(songId: String){
        val tempDownload = tempDownloads[songId]?:return

        val updateJob =tempDownload.copy( tempJob = Job())
        tempDownloads.replace(songId, updateJob)
        //downloadNotification.notificationManager.cancel (tempDownload.notificationId)
        startDownload(tempDownload.song)

    }
    private fun retryDownload(songId: String, shouldRestart:Boolean){

    }

    private fun removeFromQueue(songId:String){
//        if (!jobsHashmap.containsKey(songId)){
//            return
//        }
//        jobsHashmap.remove(songId)
    }
    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun sendCurrentDownloads(){
        val intent  = Intent(DOWNLOAD_STATUS)
        val downloads = downloadStatusHashmap.values.toList()
        val encodeToString = Json.encodeToString(downloads)
        intent.putExtra(CURRENT_DOWNLOADS,encodeToString)
        sendBroadcast(intent)
    }

    private fun sendFileResult(fileResult: FileResult){
        val intent  = Intent(DOWNLOAD_STATUS)
        val encodeToString = Json.encodeToString(fileResult)
        intent.putExtra(DOWNLOAD_RESULT,encodeToString)
        sendBroadcast(intent)
    }





//
//    private fun sendNotification(
//        notificationTitle: String,
//        notificationDescription:String,
//        notificationId:Int,
//        icon : Int= R.drawable.baseline,
//        channelId : String = DOWNLOAD_CHANNEL,
//        autoCancel:Boolean = false
//    ){
//        val newNotification = downloadNotification.buildNotification(
//            title = notificationTitle,
//            description = notificationDescription,
//            channelId = channelId,
//            icon = icon,
//            autoCancel = autoCancel
//        )
//        downloadNotification.notificationManager.notify(notificationId, newNotification)
//    }

    companion object{

        const val DOWNLOAD_ACTION ="DOWNLOAD_ACTION"
        const val START_DOWNLOAD = "START_DOWNLOAD"
        const val STOP_DOWNLOAD = "STOP_DOWNLOAD"
        const val PAUSE_DOWNLOAD = "PAUSE_DOWNLOAD"
        const val RESUME_DOWNLOAD = "RESUME_DOWNLOAD"
        const val SHOULD_RESTART = "SHOULD_RESTART"
        const val RETRY_DOWNLOAD = "RETRY_DOWNLOAD"
        const val REMOVE_FROM_QUEUE="REMOVE_FROM_QUEUE"


        const val AUDIO_URL="SONG_URL"
        const val SONG_TITLE ="SONG_TITLE"
        const val THUMBNAIL_URL = "THUMBNAIL_URL"
        const val ARTIST_USERNAME = "ARTIST_USERNAME"
        const val SONG_LENGTH = "SONG_LENGTH"
        const val CREATED_DATE = "CREATED_DATE"
        const val SONG_DESCRIPTION ="SONG_DESCRIPTION"

        const val DOWNLOAD_CHANNEL = "DOWNLOAD_CHANNEL"

        const val DOWNLOAD_STATUS = "DOWNLOAD_STATUS"
        const val DOWNLOAD_RESULT = "DOWNLOAD_RESULT"
        const val CURRENT_DOWNLOADS = "CURRENT_DOWNLOADS"

        const val GET_DOWNLOADS = "GET_DOWNLOADS"
        const val SEND_DOWNLOAD_LIST = "SEND_DOWNLOAD_LIST"
        const val DOWNLOADS = "DOWNLOADS"
        const val CURRENT_DOWNLOAD = "CURRENT_DOWNLOAD"
        const val GET_CURRENT_DOWNLOAD = "GET_CURRENT_DOWNLOAD"
    }

}