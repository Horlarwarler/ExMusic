package com.crezent.music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.crezent.common.util.CustomError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ExMusicPlayer (
    private val player: Player,
    private val context:Context
){
    private var mediaPlayer:MediaPlayer? = null
    private var downloadedMedias: List<MediaItem> = emptyList()
    private var remoteMedias: List<MediaItem> = emptyList()
    private var playingFromDownload:Boolean = false

    private fun setPlayingFromDownload(inDownload:Boolean = true){
        playingFromDownload = true
    }


    fun previewSong(uri: Uri){
        if (player.isPlaying){
            player.pause()
        }
        Log.d("Preview","Preview Will Play")

        var elapsedTime = 0
        mediaPlayer?.let {
            if (it.isPlaying){
                it.stop()
                mediaPlayer = null
            }
        }
        mediaPlayer = MediaPlayer.create(context, uri)
        mediaPlayer?.start()
        CoroutineScope(Dispatchers.IO).launch {
            while (elapsedTime < 10 && mediaPlayer?.isPlaying == true){
                delay(1000)
                elapsedTime++
            }

            if (mediaPlayer?.isPlaying == true){
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
            withContext(Dispatchers.Main){
                if (player.playbackState == Player.STATE_READY){
                    playPauseMusic()
                }

            }


        }


    }
    fun stopPreview(){
        if (mediaPlayer?.isPlaying == false) return
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        if (player.playbackState == Player.STATE_READY){
            playPauseMusic()
        }
    }
    fun setMediaItem(mediaItem: MediaItem){
        player.setMediaItem(mediaItem)
    }
    fun setMediaItems(mediaItems:List<MediaItem>){
        if (playingFromDownload){
            downloadedMedias = mediaItems
        }
        else {
            remoteMedias = mediaItems
        }
        player.setMediaItems(mediaItems)
    }


    fun forward(){
        val currentPosition = player.currentPosition
        try {
            val seekTo = currentPosition + 10000
            if (seekTo > player.contentDuration ){
                throw CustomError("Reached End Of Playing")
            }
        }
        catch (error: CustomError){
            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
            nextMusic()
        }
        catch (error:Exception){
            Toast.makeText(context, error.message?:"Unknown Error", Toast.LENGTH_LONG).show()

        }

    }
    fun rewind(){
        val currentPosition =player.currentPosition
        try {
            val seekTo = currentPosition - 10000
            if (seekTo < 0){
                throw CustomError("Reached End Of Playing")
            }
        }
        catch (error: CustomError){
          //  Toast.makeText(context, error.errorMessage, Toast.LENGTH_LONG).show()
           player.seekTo(0)
        }
        catch (error:Exception){
            Toast.makeText(context, error.message?:"Unknown Error", Toast.LENGTH_LONG).show()

        }

    }
    fun previousMusic(){
        if ( player.mediaItemCount <=1){
            if (playingFromDownload){
               player.setMediaItems(downloadedMedias)
            }
            else{
                player.setMediaItems(remoteMedias)
            }
        }
        else{
            player.seekToPreviousMediaItem()

        }
        prepare()
        player.playWhenReady = true
    }
    fun nextMusic(){
        if ( player.mediaItemCount <=1){
            if (playingFromDownload){
                player.setMediaItems(downloadedMedias)
            }
            else{
                player.setMediaItems(remoteMedias)
            }
        }
        else{
            player.seekToNextMediaItem()
        }
        player.prepare()
        player.playWhenReady = true
    }
    private fun setRemoteMediaItems(){
        val currentMedia = player.currentMediaItem
        player.seekToNextMediaItem()

        try {
            val currentPlaying = remoteMedias.find {
                it.mediaId == currentMedia!!.mediaId
            }
            val index = remoteMedias.indexOf(currentPlaying)
            var nextIndex: Int
            do {
                nextIndex =  Random.nextInt(0,remoteMedias.size-1 )
            }
            while (nextIndex == index)

            val nextPlaying  = remoteMedias[nextIndex]
            player.setMediaItem(nextPlaying)


        }
        catch (error:Exception){
            player.setMediaItem(remoteMedias.first())
        }
        finally {
            prepare()
            playPauseMusic()
        }
    }

    fun prepare(){
        player.prepare()

    }
    fun playPauseMusic(){
        try {
            val isPlaying = player.isPlaying
            if (isPlaying){
                player.pause()
            }

            else{
              //  if (player.playbackState == Player.STATE_READY)
                player.play()
            }
        }
        catch (error:Exception){
            Log.d("Play", "error ${error.message}")
        }


    }

}