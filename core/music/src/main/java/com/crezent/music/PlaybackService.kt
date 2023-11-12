package com.crezent.music

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.CommandButton
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.crezent.common.util.Constant.SONG_ID
import com.crezent.data.mapper.mapToMediaItem
import com.crezent.data.mapper.mapToSerialized
import com.crezent.data.repository.ExMusicRepo
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.ConnectException
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackService  : MediaSessionService() {
    private var mediaSession:MediaSession? = null
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return  mediaSession
    }
    val player by lazy {
        ExoPlayer.Builder(this).build()
    }

    @Inject
    lateinit var exMusicRepo: ExMusicRepo



    private var job: Job? = null

   // private var _downloadedMediaItems = MutableStateFlow<List<MediaItem>>(emptyList())
    private var mediaItems = MutableStateFlow<List<MediaItem>>(emptyList())

    var playbackState = PlaybackState()
    private val exMusicPlayer  by lazy {
        ExMusicPlayer(player, this)
    }
    private val playerListener = object  :Player.Listener {

        override fun onPlaybackStateChanged(playerState: Int) {

            playbackState = playbackState.copy(playerState = playerState)
            if (playerState == Player.STATE_ENDED && player.mediaItemCount <=1){
                exMusicPlayer.setMediaItems(mediaItems = mediaItems.value)

                exMusicPlayer.nextMusic()
            }
            else if (playerState == Player.STATE_ENDED && player.mediaItemCount >1){
                exMusicPlayer.nextMusic()
            }
            sendPlaybackState()

            super.onPlaybackStateChanged(playerState)
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            if ( !isLoading && player.isPlaying){
                playbackState = playbackState.copy(
                    currentPosition = player.currentPosition,
                    contentDuration = player.contentDuration,
                    isPlaying = player.isPlaying
                )
                sendPlaybackState()
            }
            super.onIsLoadingChanged(isLoading)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying){
                updateCurrentDuration()
            }

            else{
                job?.cancel()
                job = null
            }
            playbackState = playbackState.copy(isPlaying = isPlaying)
            sendPlaybackState()
            super.onIsPlayingChanged(isPlaying)
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            mediaItem?.let {
                val serializedMediaItem = it.mapToSerialized()
                playbackState = playbackState.copy(
                    mediaItem = serializedMediaItem,
                    currentPosition = 0,
                    contentDuration = 0
                    )
                sendPlaybackState()

            }
            super.onMediaItemTransition(mediaItem, reason)
        }
    }

    private val mediaSessionCallback = @UnstableApi object : MediaSession.Callback{
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo
        ): MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            return MediaSession.ConnectionResult.accept(connectionResult.availableSessionCommands,connectionResult.availablePlayerCommands)
        }

        override fun onPostConnect(session: MediaSession, controller: MediaSession.ControllerInfo) {
            val playPauseButton = CommandButton.Builder()
                .setDisplayName(if (session.player.isPlaying)"Pause" else "Play")
             //   .setIconResId(if (session.player.isPlaying) R.drawable.baseline_play_arrow_24 else R.drawable.baseline_pause_24)
                .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
                .build()
            val nextButton = CommandButton.Builder()
                .setDisplayName("Next")
              //  .setIconResId(R.drawable.next)
                .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT)
                .build()
            val previousButton = CommandButton.Builder()
                .setDisplayName("Previous")
             //   .setIconResId(R.drawable.previous)
                .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS)
                .build()
            val shuffleButton = CommandButton.Builder()
                .setDisplayName(if (session.player.shuffleModeEnabled)"UnShuffled" else "Shuffled")
               // .setIconResId(R.drawable.shuffle)
                .setPlayerCommand(Player.COMMAND_SET_SHUFFLE_MODE)
                .build()
            val loopButton = CommandButton.Builder()
                .setDisplayName("Loop")
               // .setIconResId(R.drawable.loop)
                .setPlayerCommand(Player.COMMAND_SET_REPEAT_MODE)
                .build()
            val commands = listOf(playPauseButton,nextButton, previousButton, shuffleButton, loopButton )
            session.setCustomLayout(controller, commands)
            super.onPostConnect(session, controller)
        }


        override fun onPlayerCommandRequest(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            playerCommand: Int
        ): Int {
            when(playerCommand){
                Player.COMMAND_SEEK_FORWARD -> {
                  exMusicPlayer.forward()
                }
                Player.COMMAND_SEEK_BACK ->{
                    exMusicPlayer.rewind()

                }
                Player.COMMAND_SEEK_TO_NEXT -> {
                   exMusicPlayer.nextMusic()
                }

                Player.COMMAND_SEEK_TO_PREVIOUS ->{
                    exMusicPlayer.previousMusic()
                }

                Player.COMMAND_PLAY_PAUSE ->{
                  exMusicPlayer.playPauseMusic()
                }
                Player.COMMAND_SET_SHUFFLE_MODE -> {
                   setShuffleMode()
                }

                Player.COMMAND_SET_REPEAT_MODE ->{
                    setRepeatMode()
                }



            }
            return super.onPlayerCommandRequest(session, controller, playerCommand)
        }



        override fun onAddMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>
        ): ListenableFuture<MutableList<MediaItem>> {
            return super.onAddMediaItems(mediaSession, controller, mediaItems)
        }

        override fun onSetMediaItems(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo,
            mediaItems: MutableList<MediaItem>,
            startIndex: Int,
            startPositionMs: Long
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            return super.onSetMediaItems(
                mediaSession,
                controller,
                mediaItems,
                startIndex,
                startPositionMs
            )
        }



        override fun onPlaybackResumption(
            mediaSession: MediaSession,
            controller: MediaSession.ControllerInfo
        ): ListenableFuture<MediaSession.MediaItemsWithStartPosition> {
            job?.cancel()
            job = null
            updateCurrentDuration()
            return super.onPlaybackResumption(mediaSession, controller)
        }


    }

    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(mediaSessionCallback)
            .build()
        player.addListener(playerListener)
        getMediaItems()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val musicAction = intent?.getStringExtra(MUSIC_ACTION)
        val songId = intent?.getStringExtra(SONG_ID)

        val previewUri  = intent?.getStringExtra(SONG_URI)
        when(musicAction){
            PLAY_PAUSE -> {
                exMusicPlayer.playPauseMusic()
            }
            REWIND -> {
                exMusicPlayer.rewind()
            }
            FORWARD -> {
                exMusicPlayer.forward()
            }

            NEXT -> {
                exMusicPlayer.nextMusic()
            }
            PREVIOUS ->{
                exMusicPlayer.previousMusic()
            }
            SHUFFLE -> {
                setShuffleMode()
            }
            REPEAT_MODE -> {
                setRepeatMode()
            }

            PLAY_SONG_BY_ID -> {
                playSongById(songId = songId!!)
            }

            PREVIEW_SONG -> previewSong(previewUri!!)

            STOP_PREVIEW -> stopPreview()


        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateCurrentDuration(){
        job =  CoroutineScope(Dispatchers.Main)
            .launch {
                while (player.isPlaying && isActive){
                    delay(1000)
                    playbackState = playbackState.copy(currentPosition = player.currentPosition)
                    sendPlaybackState()
                }
            }
    }


    private fun getMediaItems() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
//            exMusicRepo.getSongs()
//                .collectLatest {
//                    when(it) {
//                        is RequestResult.Success -> {
//                            val mediaItems = it.resource.map { songDto ->
//                                songDto.mapToMediaItem()
//                            }
//                            this@PlaybackService.mediaItems.update {
//                                mediaItems
//                            }
//                            sendMediaItem(mediaItems)
//
//                        }
//                        is RequestResult.Loading -> {
//                            Log.d("TAG REQUEST", "Resource loading")
//
//
//                        }
//                        is RequestResult.Error ->{
//                            Log.d("TAG REQUEST", "Resource error ${it.errorMessage}")
//
//                        }
//                    }
//
//                }

        }
    }

    private fun playSongById(songId:String){


        val playingSongId = player.currentMediaItem?.mediaId
        if (playingSongId == songId){
            sendPlaybackState()
            return
        }
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                val mediaItem =  exMusicRepo.getSongById(songId)?.mapToMediaItem()!!
                withContext(Dispatchers.Main){
                    exMusicPlayer.setMediaItem(mediaItem)
                    exMusicPlayer.prepare()
                    exMusicPlayer.playPauseMusic()
                }

            }
            catch (error:ConnectException){

            }
            catch (error:Exception){

            }

        }
    }

    private fun previewSong(songUriString: String){
        Log.d("Preview","Will Preview")

        val uri = Uri.parse(songUriString)
        exMusicPlayer.previewSong(uri)
        Log.d("Preview","Selected uri String $songUriString")

    }
    private fun stopPreview(){
        exMusicPlayer.stopPreview()
    }

    private fun setRepeatMode(){
        when(player.repeatMode){
            Player.REPEAT_MODE_OFF -> {
                player.repeatMode = Player.REPEAT_MODE_ONE
            }
            Player.REPEAT_MODE_ONE -> {
                player.repeatMode = Player.REPEAT_MODE_ALL
            }
            Player.REPEAT_MODE_ALL -> {
                player.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
        playbackState = playbackState.copy(repeatMode = player.repeatMode)
        sendPlaybackState()
    }

    private fun setShuffleMode(){
        player.shuffleModeEnabled = !player.shuffleModeEnabled
        playbackState = playbackState.copy(shuffle = player.shuffleModeEnabled)
        sendPlaybackState()
    }
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private fun sendPlaybackState(){
        val encodeStateToString = Json.encodeToString(playbackState)
        val intent = Intent().setAction(SEND_PLAYER_STATE)
        intent.putExtra(PLAY_BACK_STATE, encodeStateToString)
        sendBroadcast(intent)
    }

    private fun sendMediaItem(remoteMedias: List<MediaItem>){
        val serializedMediaItems = remoteMedias.map {
            it.mapToSerialized()
        }
        val encodeStateToString = Json.encodeToString(serializedMediaItems)
        val intent = Intent().setAction(SEND_PLAYER_STATE)
        intent.putExtra(MEDIAS, encodeStateToString)
        sendBroadcast(intent)
    }

    companion object {
        // PLayer action
        const val PLAY_PAUSE = "PLAY_PAUSE"
        const val REWIND = "REWIND"
        const val FORWARD = "FORWARD"
        const val NEXT ="NEXT"
        const val PREVIOUS = "PREVIOUS"
        const val SHUFFLE = "SHUFFLE"
        const val REPEAT_MODE = "REPEAT_MODE"

        //Service Action
        const val MUSIC_ACTION = "MUSIC_ACTION"

        //Media Items Actions

        const val PLAY_SONG_BY_ID = "PLAY_SONG_BY_ID"

        const val PLAY_BACK_STATE = "PLAY_BACK_STATE"
        const val SEND_PLAYER_STATE ="SEND_PLAYER_STATE"
        const val SEND_REMOTE_MEDIAS ="SEND_REMOTE_MEDIAS"
        const val MEDIAS ="MEDIAS"

        const val PREVIEW_SONG  = "PREVIEW_SONG"
        const val SONG_URI = "SONG_URI"
        const val STOP_PREVIEW  = "STOP_PREVIEW"



    }
}