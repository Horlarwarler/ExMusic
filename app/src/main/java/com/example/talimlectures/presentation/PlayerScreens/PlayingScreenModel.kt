package com.example.talimlectures.presentation.PlayerScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.Favourite
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.data.local.repository.LectureRepository
import com.example.talimlectures.domain.download.DownloadInterface
import com.example.talimlectures.domain.music.MusicInterface
import com.example.talimlectures.util.PlayAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PlayingScreenModel @Inject constructor(
    private  val downloadInterface: DownloadInterface,
    private val musicInterface: MusicInterface,
    private val lectureRepository: LectureRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    var playingScreenState by mutableStateOf(PlayingScreenState())
    val musicState by mutableStateOf(musicInterface.musicState)
    val downloadState by mutableStateOf(downloadInterface.downloadState)


    fun miniPlayerAction(action: PlayAction){
        when(action){
            PlayAction.FORWARD ->{
                forwardMusic()
            }
            PlayAction.RESUME ->{
                resumePlaying()
            }
            PlayAction.NEXT ->{
                nextMusic()
            }
            PlayAction.PAUSE ->{
                println("I have paused")
                onPausedMusic()
            }
            PlayAction.PREVIOUS ->{
                previousMusic()
            }
            PlayAction.REWIND ->{
                rewindMusic()
            }
            PlayAction.STOP ->{
                onStopPlaying()
            }
            else -> {

            }
        }


    }
    fun handleChangeEvent(events: PlayingScreenEvent){
        val selectedLecture = musicState.currentLecture!!
        when(events){


            is PlayingScreenEvent.OnFavouriteClick ->{

                onFavouriteClicked(isFavourite = events.isFavourite, selectedLecture = selectedLecture)
            }
            is PlayingScreenEvent.OnDownloadClick->{
                startDownload(selectedLecture.lectureTitle)
            }
            is PlayingScreenEvent.StopDownload ->{
                stopDownload(selectedLecture.lectureTitle)
            }
            is PlayingScreenEvent.Resume ->{
                resumePlaying()
            }
            is PlayingScreenEvent.Pause ->{
                onPausedMusic()
            }
            is PlayingScreenEvent.FastForward ->{
                forwardMusic()
            }
            is PlayingScreenEvent.Rewind ->{
                rewindMusic()
            }
            is PlayingScreenEvent.Next ->{
                nextMusic()
            }
            is PlayingScreenEvent.Previous ->{
                previousMusic()
            }


        }
    }
    private fun onStopPlaying(){
        musicInterface.stopMusic()
    }
    private fun onPausedMusic(){
        musicInterface.pauseMusic()
    }
    private fun resumePlaying(){
        viewModelScope.launch {
            musicInterface.resumeMusic()
        }
    }
    private fun rewindMusic(){
        viewModelScope.launch {
            musicInterface.skipBackward()
        }
    }
    private  fun forwardMusic(){
        viewModelScope.launch {
            musicInterface.fastForward()
        }
    }
    private  fun nextMusic(){
        viewModelScope.launch {
            musicInterface.nextMusic()
        }
    }
    private fun previousMusic(){
        viewModelScope.launch {
            musicInterface.previousMusic()
        }
    }
    private fun startDownload(selectedLecture: String){
        downloadInterface.selectLecture(selectedLecture)
        viewModelScope.launch {
            downloadInterface.download()
        }
    }

    private fun stopDownload(selectedLecture: String){
        downloadInterface.selectLecture(selectedLecture)
        viewModelScope.launch {
            downloadInterface.stopDownload()
        }
    }

    private fun onFavouriteClicked(selectedLecture: DatabaseLectureModel, isFavourite:Boolean){
        viewModelScope.launch {
            if(isFavourite){
                    lectureRepository.deleteFavourite(selectedLecture.lectureTitle)
            }
            else{
                val favourite = Favourite(lectureTitle = selectedLecture.lectureTitle)
                lectureRepository.addFavourite(favourite = favourite)
            }
        }
    }

}