package com.example.talimlectures.presentation.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talimlectures.data.model.DatabaseLectureModel
import com.example.talimlectures.data.local.repository.LectureRepository
import com.example.talimlectures.domain.download.DownloadInterface
import com.example.talimlectures.domain.internet.InternetConnectionImpl
import com.example.talimlectures.domain.internet.NetworkState
import com.example.talimlectures.domain.music.MusicInterface
import com.example.talimlectures.util.LectureType
import com.example.talimlectures.util.PlayAction
import com.example.talimlectures.util.Resource
import com.example.talimlectures.util.convertLectureToLectureType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeScreenModel @Inject constructor(
    private  val downloadInterface: DownloadInterface,
    private val  musicInterface: MusicInterface,
    private val lectureRepository: LectureRepository,
    private val savedStateHandle: SavedStateHandle,
    private val internetConnectionImpl: InternetConnectionImpl

):ViewModel(){
     var homeScreenState    by mutableStateOf(HomeScreenState())
    val musicState by mutableStateOf(musicInterface.musicState)
    val downloadState by mutableStateOf(downloadInterface.downloadState)
    private var job: Job? = null



    fun initialized(){
        internetConnectionImpl.observeForever {
            homeScreenState = when(it){
                NetworkState.isConnected ->{
                    homeScreenState.copy(isConnected = true)
                }
                NetworkState.isDisconnected ->{
                    homeScreenState.copy(isConnected = false)
                }
            }
        }
        viewModelScope.launch {
            val lectures = launch {
                getAllLecture(homeScreenState.searchText)
            }
            lectures.join()
            async {
                getNewAddedLectures()
                getRecentlyPlayed()
                favouriteLectures()

            }

        }
    }

    private  fun getAllLecture(searchQuery: String,isSearching:Boolean = false){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getAllLecture(searchQuery).collect{resource ->
                when(resource){
                    is Resource.Success ->{
                        homeScreenState = if(isSearching){
                            homeScreenState.copy( searchResult = resource.data!!)

                        } else{
                            homeScreenState.copy(lectures = resource.data!!, )

                        }
                    }
                    is Resource.Error -> {
                        homeScreenState = homeScreenState.copy(error = resource.message!!)


                    }
                    is Resource.Loading ->{
                        homeScreenState = homeScreenState.copy(isLoading = true)

                    }
                }

            }
           
        }
    }

    private  fun  getNewAddedLectures(){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getNewLecture().collect{
                    resource ->
                when(resource){
                    is Resource.Success ->{
                        val newlyAddedLectures: MutableList<DatabaseLectureModel> = mutableListOf()
                        resource.data!!.forEach {newlyAdded->
                            var lecture = homeScreenState.lectures.firstOrNull {
                                newlyAdded.lectureTitle == it.lectureTitle
                            }
                            if (lecture != null){
                                lecture = lecture.convertLectureToLectureType(LectureType.NewlyAddedType(newlyAdded))
                                newlyAddedLectures.add(lecture)
                            }

                        }
                        homeScreenState = homeScreenState.copy(newlyAdded =  newlyAddedLectures)
                    }
                    is Resource.Error -> {
                        homeScreenState = homeScreenState.copy(error = resource.message!!)

                    }
                    is Resource.Loading ->{
                        homeScreenState = homeScreenState.copy(isLoading = true)

                    }
                }
            }
        }
    }



    private fun getRecentlyPlayed(){
        viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getRecentPlayed().collect{
                    resource ->
                when(resource){
                    is Resource.Success ->{
                        val recentlyPlayed: MutableList<DatabaseLectureModel> = mutableListOf()
                        resource.data!!.forEach {recent->
                            var lecture = homeScreenState.lectures.firstOrNull {
                                recent.lectureTitle == it.lectureTitle
                            }
                            if (lecture != null){
                                lecture = lecture.convertLectureToLectureType(LectureType.RecentlyPlayedType(recent))
                                recentlyPlayed.add(lecture)
                            }

                        }
                        homeScreenState = homeScreenState.copy(newlyAdded =  recentlyPlayed)
                    }
                    is Resource.Error -> {
                        homeScreenState = homeScreenState.copy(error = resource.message!!)

                    }
                    is Resource.Loading ->{
                        homeScreenState = homeScreenState.copy(isLoading = true)

                    }
                }
            }

        }
    }
    private fun favouriteLectures(){
        val result= viewModelScope.launch(Dispatchers.IO) {
            lectureRepository.getFavourites().collect{
                    resource ->
                when(resource){
                    is Resource.Success ->{
                        val favouritesLectures: MutableList<DatabaseLectureModel> = mutableListOf()
                        resource.data!!.forEach {favourite->
                            var lecture = homeScreenState.lectures.firstOrNull {
                                favourite.lectureTitle == it.lectureTitle
                            }
                            if (lecture != null){
                                lecture = lecture.convertLectureToLectureType(LectureType.FavouriteLectureType(favourite))
                                favouritesLectures.add(lecture)
                            }

                        }
                        homeScreenState = homeScreenState.copy(newlyAdded =  favouritesLectures)
                    }
                    is Resource.Error -> {
                        homeScreenState = homeScreenState.copy(error = resource.message!!)

                    }
                    is Resource.Loading ->{
                        homeScreenState = homeScreenState.copy(isLoading = true)

                    }
                }
            }



        }
    }

    fun handleChangeEvent(events: HomeScreenEvents){
        when(events){
            is HomeScreenEvents.onTextChange ->{
                onTextChange(events.text)
            }
            is HomeScreenEvents.onSearchClick ->{
                onSearchClicked()
            }
            is HomeScreenEvents.onFavouriteClick ->{
                onFavouriteClicked(events.selectedLecture)
            }
            is HomeScreenEvents.onPlayClick ->{
                onPlayClicked(events.selectedLecture)
            }
            is HomeScreenEvents.onDownloadClick ->{
                startDownload(events.selectedLecture)
            }
            is HomeScreenEvents.stopDownload ->{
                stopDownload(events.selectedLecture)
            }
            is HomeScreenEvents.onResumeClicked ->{
                resumePlaying()
            }
            is HomeScreenEvents.onMusicPause ->{
                onPausedMusic()
            }
            is HomeScreenEvents.onStop ->{
                onStopPlaying()
            }
            is HomeScreenEvents.onNavigate ->{
                setPreviousScreen(events.screenName)
            }
        }
    }

    fun miniPlayerAction(action:PlayAction){
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
        homeScreenState = homeScreenState.copy(searchText = value)
        job?.cancel()
        job  = viewModelScope.launch {
            delay(500)
            getAllLecture(homeScreenState.searchText)
        }

    }
    private fun onSearchClicked(){
        if (homeScreenState.searchText.length > 2){
            job?.cancel()
            getAllLecture(homeScreenState.searchText)
        }

    }

    private fun setPreviousScreen(previousScreenName: String){
        savedStateHandle.set<String>("previousScreenName",previousScreenName)
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
                        homeScreenState = homeScreenState.copy(error = resource.message!!)

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

    private fun onFavouriteClicked(selectedLecture: String){

    }


}