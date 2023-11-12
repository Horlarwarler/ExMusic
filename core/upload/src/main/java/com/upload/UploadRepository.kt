package com.upload

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.crezent.common.util.RequestResult
import com.crezent.models.SongUpload
import com.crezent.network.api.CreatorApi
import com.upload.UploadService.Companion.CURRENT_UPLOAD_STATUS
import com.upload.UploadService.Companion.DESCRIPTION
import com.upload.UploadService.Companion.LENGTH
import com.upload.UploadService.Companion.ON_START
import com.upload.UploadService.Companion.SONG_URI_STRING
import com.upload.UploadService.Companion.THUMBNAIL_URI_STRING
import com.upload.UploadService.Companion.TITLE
import com.upload.UploadService.Companion.UPDATE_SONG
import com.upload.UploadService.Companion.UPLOADS_STATUS
import com.upload.UploadService.Companion.UPLOAD_ACTION
import com.upload.UploadService.Companion.UPLOAD_SONG
import com.upload.UploadService.Companion.UPLOAD_STATUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class  UploadRepository (
  private val  context:Context,
    private val creatorApi: CreatorApi
) {


    private val _uploadsStatus: MutableStateFlow<List<SongUpload>>  = MutableStateFlow(emptyList())
    val uploads = _uploadsStatus.asStateFlow()
    private val _currentUploadStatus:MutableStateFlow<SongUpload?> = MutableStateFlow(null)
    val currentUploadStatus = _currentUploadStatus.asStateFlow()
    private val broadCast  = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onReceive(context: Context?, intent: Intent?) {
           if (intent?.action == UPLOAD_STATUS){
               val uploadsStatusEncoded = intent.getStringExtra(UPLOADS_STATUS, )
               uploadsStatusEncoded?.let {
                    val uploads = Json.decodeFromString<List<com.crezent.models.SongUpload>>(
                       it
                   )
                   _uploadsStatus.update {
                       uploads
                   }
               }
               val currentUploadEncoded = intent.getStringExtra(CURRENT_UPLOAD_STATUS)

              // val startNewUpdate = intent.getBooleanExtra(ON_START, false)

               currentUploadEncoded?.let {
                   val upload = Json.decodeFromString<com.crezent.models.SongUpload>(it)
                   Log.d("LOG", "Current Upload is  $upload ")
                   val currentUpload = Json.decodeFromString<com.crezent.models.SongUpload>(it)
                   _currentUploadStatus.update {
                       currentUpload
                   }
               }


           }
        }
    }

    fun uploadSong(
        title: String,
        description: String,
        duration: Double,
        songUri:Uri,
        thumbnailUri:Uri?
    )  {

        val intent = Intent(context, UploadService::class.java)
            .apply {
                putExtra(UPLOAD_ACTION, UPLOAD_SONG)

                putExtra(TITLE, title)
                putExtra(DESCRIPTION, description)
                putExtra(LENGTH, duration)
                putExtra(SONG_URI_STRING,songUri.toString())
                thumbnailUri?.let {
                    putExtra(THUMBNAIL_URI_STRING, thumbnailUri.toString())
                }
            }


        context.startService(intent)
        registerBroadcast()
        Log.d("LOG","SONG ACTION is 2 ")

    }
    fun updateSong(
        songId: String,
        title: String,
        description: String?,
        length: Double?,
        songUri:Uri?,
        thumbnailUri:Uri?
    ) {

        val intent = Intent()
        intent.putExtra(UPLOAD_ACTION, UPDATE_SONG)
        intent.putExtra(THUMBNAIL_URI_STRING, thumbnailUri?.toString())
        intent.putExtra(TITLE, title)
        intent.putExtra(DESCRIPTION, description)
        intent.putExtra(LENGTH, length)
        intent.putExtra(SONG_URI_STRING,songUri?.toString())

        context.startService(intent)
    }

     fun deleteSong(songId: String): Flow<RequestResult<Unit>> {
        return creatorApi.deleteSong(songId)
    }

    fun createPlaylist(playlistName: String): Flow<RequestResult<com.crezent.models.ArtistPlaylist>> {
        return creatorApi.createPlaylist(playlistName)
    }

    fun addToPlaylist(songId: String): Flow<RequestResult<Unit>> {
        return creatorApi.addToPlaylist(songId)
    }

    fun getAllPlaylist(): Flow<RequestResult<Unit>> {
        /// return creatorApi.
        TODO()
    }

    fun deletePlaylist(playlistId: String): Flow<RequestResult<Unit>> {
        return creatorApi.deletePlaylist(playlistId)
    }
    fun onStart(){
        val intent = Intent(context, UploadService::class.java)
        intent.putExtra(UPLOAD_ACTION, ON_START)
        context.startService(intent)
    }


     fun registerBroadcast(){
        val intentFilter = IntentFilter(UPLOAD_STATUS)
         context.registerReceiver(broadCast, intentFilter)
    }

    fun unregisterBroadcast(){
        context.unregisterReceiver(broadCast)
    }





}