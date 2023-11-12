package com.crezent.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import com.crezent.common.util.Constant.SONG_ID
import com.crezent.models.MediaItem
import com.crezent.music.PlaybackService.Companion.FORWARD
import com.crezent.music.PlaybackService.Companion.MEDIAS
import com.crezent.music.PlaybackService.Companion.MUSIC_ACTION
import com.crezent.music.PlaybackService.Companion.NEXT
import com.crezent.music.PlaybackService.Companion.PLAY_BACK_STATE
import com.crezent.music.PlaybackService.Companion.PLAY_PAUSE
import com.crezent.music.PlaybackService.Companion.PLAY_SONG_BY_ID
import com.crezent.music.PlaybackService.Companion.PREVIEW_SONG
import com.crezent.music.PlaybackService.Companion.PREVIOUS
import com.crezent.music.PlaybackService.Companion.REPEAT_MODE
import com.crezent.music.PlaybackService.Companion.REWIND
import com.crezent.music.PlaybackService.Companion.SEND_PLAYER_STATE
import com.crezent.music.PlaybackService.Companion.SHUFFLE
import com.crezent.music.PlaybackService.Companion.SONG_URI
import com.crezent.music.PlaybackService.Companion.STOP_PREVIEW
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class   PlaybackRepository (
    private val context: Context,
){

    private var _mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    val mediaItems= _mediaItems.asStateFlow()
    private var _playbackState= MutableStateFlow(PlaybackState())
    val playbackState= _playbackState.asStateFlow()


    private val playbackStateBroadcast = object  : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val encodedPlaybackState  = intent?.getStringExtra(PLAY_BACK_STATE)
            encodedPlaybackState?.let {
                encodedString ->
                val playbackState = Json.decodeFromString<PlaybackState>(encodedString)
                _playbackState.update {
                    playbackState
                }
            }

            val encodedMediaItems  = intent?.getStringExtra(MEDIAS)
            encodedMediaItems?.let {
                    encodedString ->
                val medias = Json.decodeFromString<List<com.crezent.models.MediaItem>>(encodedString)
                _mediaItems.update {
                  medias
                }
            }


        }
    }

    init {
        val intent = Intent(context, PlaybackService::class.java)
        context.startService(intent)
        val intentFilter = IntentFilter(SEND_PLAYER_STATE)
        context.registerReceiver(playbackStateBroadcast, intentFilter)
    }

    fun playSongById(songId: String){
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, PLAY_SONG_BY_ID)
        intent.putExtra(SONG_ID, songId)
        context.startService(intent)
    }

     fun playPauseMusic(){
         val intent = Intent(context, PlaybackService::class.java)

         intent.putExtra(MUSIC_ACTION, PLAY_PAUSE)
         context.startService(intent)
    }

     fun nextMusic(){
         val intent = Intent(context, PlaybackService::class.java)
         intent.putExtra(MUSIC_ACTION, NEXT)
         context.startService(intent)
    }

     fun previousMusic(){
         val intent = Intent(context, PlaybackService::class.java)
         intent.putExtra(MUSIC_ACTION, PREVIOUS)
         context.startService(intent)
    }


    fun setShuffleMode(){
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, SHUFFLE)
        context.startService(intent)
    }

     fun setRepeatMode(){
         val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, REPEAT_MODE)
        context.startService(intent)
    }


    fun forward(){
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, FORWARD)
        context.startService(intent)
    }

    fun rewind(){
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, REWIND)
        context.startService(intent)

    }

    fun previewSong(uri:Uri){
        Log.d("Preview","Will Preview from repo")
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, PREVIEW_SONG)
        intent.putExtra(SONG_URI, uri.toString())
        context.startService(intent)
    }

    fun stopPreview(){
        val intent = Intent(context, PlaybackService::class.java)
        intent.putExtra(MUSIC_ACTION, STOP_PREVIEW)
        context.startService(intent)
    }






    fun onStop(){
        context.unregisterReceiver(playbackStateBroadcast)

    }


}