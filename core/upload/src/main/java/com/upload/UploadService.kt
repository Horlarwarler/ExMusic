package com.upload


import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.crezent.common.util.Constant.SONG_ID
import com.crezent.common.util.FileHelper
import com.crezent.common.util.RequestResult
import com.crezent.common.util.extractStringFromTime
import com.crezent.common.util.extractTimeFromString
import com.crezent.common.util.generateTodayDate
import com.crezent.common.util.increaseTime
import com.crezent.network.api.CreatorApi
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.util.generateNonce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Timer
import javax.inject.Inject

@AndroidEntryPoint
class UploadService  : Service() {


    @Inject
    lateinit var creatorApi: CreatorApi


    private val fileHelper  by lazy {
        FileHelper(this)
    }

    private val timer = Timer()

    private var uploadHashMap :HashMap<String, com.crezent.models.SongUpload> = hashMapOf()

    //private var jobHashmap:HashMap<String,Job> = hashMapOf()

    private var job:Job? = null
    init {
        Log.d("LOG","Service Initiated")

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val action = intent?.getStringExtra(UPLOAD_ACTION)
        val songId = intent!!.getStringExtra(SONG_ID)
        val title = intent.getStringExtra(TITLE)
        val description = intent.getStringExtra(DESCRIPTION)


        val length = intent.getDoubleExtra(LENGTH,0.0)
        val thumbnailUriString = intent.getStringExtra(THUMBNAIL_URI_STRING)
        val songUriString = intent.getStringExtra(SONG_URI_STRING)

        Log.d("LOG","Action is $action")


        when(action) {

            UPLOAD_SONG -> {
                uploadSong(
                    title = title!!,
                    description = description!!,
                    length = length,
                    songUriString = songUriString!!,
                    thumbnailUriString =  thumbnailUriString
                )
            }
            UPDATE_SONG -> {
                updateSong(
                    songId = songId!!,
                    title = title!!,
                    description = description,
                    length = length,
                    songUriString = songUriString,
                    thumbnailUriString =  thumbnailUriString
                )
            }

            CREATE_PLAYLIST -> TODO()
            EDIT_PLAYLIST -> TODO()
            DELETE_PLAYLIST -> TODO()
        }
        return START_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadSong(
        title:String,
        description:String,
        length: Double,
        songUriString:String,
        thumbnailUriString: String?) {

        val tempId = generateNonce()

        val songArray  = fileHelper.byteFromUri(Uri.parse(songUriString))?:return

        val thumbnailArray =  thumbnailUriString?.let {
            val thumbnailUri = Uri.parse(it)
            fileHelper.byteFromUri(thumbnailUri)
        }

      //  uploadHashMap[tempId] = uploadJob
        val songUpload = com.crezent.models.SongUpload(
            songId = null,
            tempSongId = tempId,
            title = title,
            thumbnailUriString = thumbnailUriString,
            songUriString = songUriString,
            uploadDate = generateTodayDate()
        )
        uploadHashMap[tempId] = songUpload




       // jobHashmap[tempId] = job
        sendUploads()
        Log.d("LOG","Service Started")
        startJob()
        CoroutineScope(Dispatchers.IO)
            .launch {
                creatorApi.uploadSong(
                    title = title,
                    description = description,
                    length = length,
                    songByteArray = songArray,
                    thumbnailByteArray = thumbnailArray
                ).collectLatest {
                        result ->
                        when(result) {
                            is  RequestResult.Loading -> {
                                val progress = result.progress?:0
                                uploadHashMap.replace(tempId,songUpload.copy(status = com.crezent.models.TaskStatus.ONGOING, progress = progress))
                                sendUploads()
                            }

                            is RequestResult.Success -> {
                                uploadHashMap.replace(tempId,songUpload.copy(status = com.crezent.models.TaskStatus.DONE, progress = 100))
                                sendUploads()
                                delay(1500)
                                uploadHashMap.remove(tempId)
                                sendUploads()
                                Log.d("LOG","Service Downloaded")

                            }

                            is RequestResult.Error -> {
                                uploadHashMap.replace(tempId,songUpload.copy(status = com.crezent.models.TaskStatus.ERROR, errorMessage = result.errorMessage))
                               // uploadHashMap.remove(currentUpload.songId)
                                sendUploads()
                                Log.d("LOG","Service ERROR ${result.errorMessage}")

                            }

                        }
                    }
            }

    }
    private fun updateSong(
        songId:String,
        title: String,
        description: String?,
        length: Double?,
        songUriString: String?,
        thumbnailUriString: String?) {
        val songTitle  = if (title.first() == '$')null else title // So we can use null value if the title is same
        val displayTitle =  title.removePrefix("$")
        val thumbnailArray =  thumbnailUriString?.let {
            val thumbnailUri = Uri.parse(it)
            fileHelper.byteFromUri(thumbnailUri)
        }

        val songArray =  songUriString?.let {
            val songUri = Uri.parse(it)
            fileHelper.byteFromUri(songUri)
        }

        val songUpload = com.crezent.models.SongUpload(
            songId = songId,
            title = displayTitle,
            songUriString = songUriString,
            thumbnailUriString = thumbnailUriString,
            uploadDate = generateTodayDate()
        )
        uploadHashMap[songId] = songUpload
        sendUploads()
        CoroutineScope(Dispatchers.IO ).launch {
            creatorApi.updateSong(
                    songId = songId,
                    title = songTitle,
                    description = description,
                    length = length,
                    songByteArray = songArray,
                    thumbnailByteArray  = thumbnailArray
            ).collectLatest {
                        result ->
                        when(result) {
                            is  RequestResult.Loading -> {
                                val progress = result.progress?:0
                                uploadHashMap.replace(songId, songUpload.copy(
                                    status = com.crezent.models.TaskStatus.ONGOING,
                                    progress = progress
                                ))
                                sendUploads()
                            }

                            is RequestResult.Success -> {
                                uploadHashMap.replace(songId,songUpload.copy(status = com.crezent.models.TaskStatus.DONE, progress = 100))
                                sendUploads()
                                delay(1500)
                                uploadHashMap.remove(songId)
                                sendUploads()
                                Log.d("LOG","Service Downloaded")

                            }

                            is RequestResult.Error -> {
                                uploadHashMap.replace(songId    ,songUpload.copy(status = com.crezent.models.TaskStatus.ERROR, errorMessage = result.errorMessage))
                                // uploadHashMap.remove(currentUpload.songId)
                                sendUploads()
                                Log.d("LOG","Service ERROR ${result.errorMessage}")

                            }
                        }
                    }
            }
    }

    private fun startJob(){
        job?.let {
            return
        }
        job =  CoroutineScope(Dispatchers.Main).launch {
            while (isActive && uploadHashMap.isNotEmpty()){
                try {
                    delay(1000)
                    uploadHashMap.onEach {
                            (songId, currentUpload) ->
                       val newTime = currentUpload.elapsedTime.extractTimeFromString().increaseTime().extractStringFromTime()
                        val newProgress = currentUpload.copy(elapsedTime = newTime)
                        uploadHashMap[songId] = newProgress
                        sendUploads()
                    }
                }
                catch (error:Exception){
                    Log.d("LOG", "ERROR  ${error.cause?.message}")
                }



            }
        }
    }

//    private fun sendCurrentUpload(fileResult: com.crezent.core.data.download.FileResult){
//        val intent = Intent(UPLOAD_STATUS)
//        val encodeToString = Json.encodeToString(value = fileResult)
//        intent.putExtra(CURRENT_UPLOAD_STATUS,encodeToString)
//        sendBroadcast(intent)
//
//    }

//    private fun onStart(){
//        fileResult?.let {
//            fileResult = null
//        }
//        val intent = Intent(UPLOAD_STATUS)
//            .putExtra(ON_START, true)
//        sendBroadcast(intent)
//
//    }
    private fun sendUploads() {
        val intent = Intent(UPLOAD_STATUS)
        val uploads = uploadHashMap.values.toList()
        val encodeToString = Json.encodeToString(uploads)
        intent.putExtra(UPLOADS_STATUS,encodeToString )
        sendBroadcast(intent)

    }

    override fun onDestroy() {
        stopSelf()
        super.onDestroy()
    }



    companion object {
        const val UPLOAD_ACTION = "UPLOAD_ACTION"

        const val UPLOAD_SONG = "UPLOAD_SONG"
        const val UPDATE_SONG = "EDIT_SONG"
        const val CREATE_PLAYLIST = "CREATE_PLAYLIST"
        const val EDIT_PLAYLIST = "EDIT_PLAYLIST"
        const val DELETE_PLAYLIST = "DELETE_PLAYLIST"
        const val UPLOAD_STATUS = "UPLOAD_STATUS"

        const val CURRENT_UPLOAD_STATUS = "UPLOAD_RESULT"
        const val UPLOADS_STATUS ="UPLOADING"


        const val THUMBNAIL_URI_STRING ="thumbnail_uri_string"
        const val SONG_URI_STRING ="SONG_ARRAY"
        const val DESCRIPTION ="DESCRIPTION"
        const val TITLE ="TITLE"
        const val LENGTH ="LENGTH"
        const val CREATED_DATE = "CREATED_DATE"
        const val ARTIST_USERNAME = "ARTIST_USERNAME"
        const val ON_START ="ON_START"


    }



}