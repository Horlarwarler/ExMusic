package com.example.talimlectures.presentation.LectureScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.local.repository.LectureRepository
import com.example.talimlectures.domain.download.DownloadInterface
import com.example.talimlectures.domain.music.MusicInterface
import com.example.talimlectures.util.PlayAction
import com.example.talimlectures.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class LectureScreenModel @Inject constructor(
    private  val musicInterface: MusicInterface,
    private val downloadInterface:DownloadInterface,
    private val lectureRepository: LectureRepository,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {
    var lectureScreenState by mutableStateOf(LectureScreenState())
    val musicState by mutableStateOf(musicInterface.musicState)
    val downloadState by mutableStateOf(downloadInterface.downloadState)
    private var job: Job? = null


    fun initialized(categoryId:Int? = null){
       getAllLecture(categoryId)
    }


    private  fun getAllLecture(categoryId: Int? = null, isSearching:Boolean = false){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getAllLecture(lectureScreenState.searchText).collect{ resource ->
                when(resource){
                    is Resource.Success ->{
                        lectureScreenState = if(isSearching){
                            lectureScreenState.copy( searchResult = resource.data!!)

                        } else{
                            if(categoryId != null){
                                lectureScreenState.copy(lectures = resource.data!!.filter { lecture->
                                    lecture.categoryId == categoryId
                                })
                            }
                            else{
                                lectureScreenState.copy(lectures = resource.data!!, )
                            }

                        }
                    }
                    is Resource.Error -> {
                        lectureScreenState = lectureScreenState.copy(error = resource.message!!)


                    }
                    is Resource.Loading ->{
                        lectureScreenState = lectureScreenState.copy(isLoading = true)

                    }
                }
//                }
            }
        }
    }

    fun handleChangeEvent(events: LectureScreenEvent){
        when(events){
            is LectureScreenEvent.onTextChange ->{
                onTextChange(events.text)
            }
            is LectureScreenEvent.onSearchClick ->{
                onSearchClicked()
            }

            is LectureScreenEvent.onPlayClick ->{
                onPlayClicked(events.selectedLecture)
            }
            is LectureScreenEvent.onDownloadClick ->{
                startDownload(events.selectedLecture)
            }
            is LectureScreenEvent.stopDownload ->{
                stopDownload(events.selectedLecture)
            }
            is LectureScreenEvent.onResumeClicked ->{
                resumePlaying()
            }
            is LectureScreenEvent.onMusicPause ->{
                onPausedMusic()
            }
            is LectureScreenEvent.onStop ->{
                onStopPlaying()
            }
            is LectureScreenEvent.onNavigate ->{
                setPreviousScreen(events.screenName)
            }
        }
    }

    private fun setPreviousScreen(previousScreenName: String){
        savedStateHandle.set<String>("previousScreenName",previousScreenName)
    }
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
    private fun onTextChange(value: String){
        lectureScreenState = lectureScreenState.copy(searchText = value)
        job?.cancel()
        job  = viewModelScope.launch {
            delay(500)
            getAllLecture(isSearching = true)

        }

    }
    private fun onSearchClicked(){
        if (lectureScreenState.searchText.length > 2){
            job?.cancel()
        }

    }

    private fun onPlayClicked(selectedLecture: String){

        viewModelScope.launch {
            lectureRepository.getSelectedLecture(selectedLecture).collect { resource->
                when(resource){
                    is Resource.Success ->{
                        musicInterface.selectMusic(resource.data!!)
                        musicInterface.playMusic()
                    }
                    is Resource.Error ->{
                        lectureScreenState = lectureScreenState.copy(error = resource.message!!)

                    }
                    is Resource.Loading ->{

                    }
                }
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

}