package com.crezent.user_presentation.playerScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.MusicIconEvent
import com.crezent.download.DownloadEvent
import com.crezent.download.DownloadServiceRepository
import com.crezent.music.PlaybackRepository
import com.crezent.user_domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PlayerScreenModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val playbackRepository: PlaybackRepository,
    private val savedStateHandle: SavedStateHandle,
    private val downloadRepository: DownloadServiceRepository,
) : ViewModel(){

    private var _playerScreenState = MutableStateFlow(PlayerScreenState())
    private val songId = savedStateHandle.get<String>("songId")!!

    private val _uiEvent =  Channel<com.crezent.common.util.UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    val playerScreenState =  combine(
        playbackRepository.playbackState,
        downloadRepository.currentDownloadResult

    ){
         playbackState,currentDownload ->

        _playerScreenState.value.copy(
            currentDuration = playbackState.currentPosition,
            totalDuration = playbackState.contentDuration,
            repeatMode = playbackState.repeatMode,
            isShuffle = playbackState.shuffle,
            isPlaying = playbackState.isPlaying,
            playingMediaItem = playbackState.mediaItem,
            isDownloaded =playbackState.mediaItem?.isDownloaded?:false,
            currentDownload = currentDownload,

        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _playerScreenState.value)


    init {
        playbackRepository.playSongById(songId)

    }
    fun handlePlayScreenAction(downloadEvent: DownloadEvent){
        val mediaItem = playerScreenState.value.playingMediaItem!!
        val duration = playerScreenState.value.totalDuration.toDouble()
        when(downloadEvent) {
            is DownloadEvent.StartDownload -> {
                downloadRepository.startDownload(mediaItem = mediaItem, duration = duration)
            }
            is DownloadEvent.PauseDownload ->{
                val songId = downloadEvent.songId
                downloadRepository.pauseDownload(songId)
            }
            is DownloadEvent.RetryDownload ->{
                val songId = downloadEvent.songId
                downloadRepository.retryDownload(songId = songId)
            }
            is DownloadEvent.StopDownload ->{
                val songId = downloadEvent.songId
                downloadRepository.stopDownload(songId)
            }
            is DownloadEvent.RemoveFromDownload ->{
                val songId = downloadEvent.songId
                deleteFromDownload(songId)
            }
            is DownloadEvent.ResumeDownload -> {
                val songId = downloadEvent.songId
                downloadRepository.resumeDownload(songId)
            }

        }
    }

    fun musicAction(action: MusicIconEvent){
        when(action){
            MusicIconEvent.FORWARD ->{
                forwardMusic()
            }

            MusicIconEvent.NEXT ->{
                nextMusic()
            }
            MusicIconEvent.PLAY_PAUSE ->{
                playPause()
            }
            MusicIconEvent.PREVIOUS ->{
                previousMusic()
            }
            MusicIconEvent.REWIND ->{
                rewindMusic()
            }
            MusicIconEvent.REPEAT -> {
                setRepeatMode()
            }
            MusicIconEvent.RESTART ->{

            }
            MusicIconEvent.SHUFFLE -> {
                setShuffleMode()
            }
            MusicIconEvent.STOP ->{

            }


        }


    }


    private fun playPause(){
       playbackRepository.playPauseMusic()
    }
    private fun rewindMusic(){
       playbackRepository.rewind()
    }
    private  fun forwardMusic(){
      playbackRepository.forward()
    }
    private  fun nextMusic(){
       playbackRepository.nextMusic()
    }
    private fun previousMusic(){
      playbackRepository.previousMusic()
    }

    private fun setShuffleMode(){
        playbackRepository.setShuffleMode()
    }

    private fun setRepeatMode(){
        playbackRepository.setRepeatMode()
    }

    private fun List<com.crezent.models.FileResult>.filterCurrentDownload(songId:String): com.crezent.models.FileResult? {
        return find {
            it.songId == songId
        }
    }
    


    private fun deleteFromDownload(songId: String){
        viewModelScope.launch {
            userUseCases.deleteSongUseCase.invoke(songId)
        }
    }

}