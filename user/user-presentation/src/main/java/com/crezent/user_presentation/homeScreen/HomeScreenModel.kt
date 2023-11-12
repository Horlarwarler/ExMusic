package com.crezent.user_presentation.homeScreen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.RequestResult
import com.crezent.common.util.removeElement
import com.crezent.domain.ApplicationUseCases
import com.crezent.user_domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class HomeScreenModel @Inject constructor(
    private val userUseCases: UserUseCases,
  private val applicationUseCases: ApplicationUseCases
    ):ViewModel(){

    var _state    = MutableStateFlow(HomeScreenState())
    var state = _state.asStateFlow()
  //  val musicState by mutableStateOf(musicInterface.musicState)
   // val message = channelMessage.consumeAsFlow()

    init {

      connect()

    }




    private fun getFollowedArtist(){
        viewModelScope.launch(Dispatchers.IO) {
//            remoteMusic.getFollowings(isArtist = true).collectLatest {
//                request ->
//                when(request){
//                    is RequestResult.Loading ->{
//                        _state.update {
//                            state.value.copy(artistIsLoading = request.isLoading)
//                        }
//                    }
//                    is RequestResult.Success ->{
//                        val followedArtist = request.resource.map {
//                            it.mapToUser()
//                        }
//
//                        _state.update {
//                            state.value.copy(followedArtist = followedArtist)
//                        }
//                        if (followedArtist.isEmpty()){
//                            getArtists()
//                        }
//                    }
//                    is RequestResult.Error ->{
//                        val errors = state.value.errors + request.errorMessage
//                        _state.update {
//                            state.value.copy(errors = errors)
//                        }
//                    }
//                    //51,207,369,366
//                }
//            }
        }
    }
    private fun connect(){
        viewModelScope.launch {
            Log.d("Socket","Connect in Viewmodel")

            applicationUseCases.connectUseCase.invoke()
        }
    }
//    private fu

    private fun getArtists(){
        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.getArtistUseCase().collectLatest {
                    request ->
                when(request){
                    is RequestResult.Loading ->{
                        _state.update {
                            state.value.copy(artistIsLoading = request.isLoading)
                        }
                    }
                    is RequestResult.Success ->{
                        val recommendedArtist = request.resource
                        _state.update {
                            state.value.copy(recommendedArtist = recommendedArtist)
                        }
                    }
                    is RequestResult.Error ->{
                        val errors = state.value.errors + request.errorMessage
                        _state.update {
                            state.value.copy(errors = errors)
                        }
                    }

                }
            }
        }
    }

    private fun getRecentlyPlayed(){
        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.recentPlayedUseCase().collectLatest {
                requestResult ->
                when(requestResult){
                    is RequestResult.Success ->{
                        val recentlyPlayed = requestResult.resource
                        _state.update {
                            state.value.copy(recentlyPlayed = recentlyPlayed)
                        }
                    }
                    is RequestResult.Error -> {
                        val errors = state.value.errors + requestResult.errorMessage
                        _state.update {
                            state.value.copy(errors = errors)
                        }

                    }
                    is RequestResult.Loading ->{
                        _state.update {
                            state.value.copy(recentIsIsLoading = requestResult.isLoading)
                        }

                    }
                }

            }


        }
    }

    private fun getTopPlaylist(){
        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.getTopPlaylistUseCase().collectLatest{
                    request ->
                when(request){
                    is RequestResult.Success ->{
                       val artistPlaylist = request.resource
                        _state.update {
                            state.value.copy(trendingSongs = emptyList(), playlistIsLoading = false)
                        }
                    }
                    is RequestResult.Error -> {
                        val errors = state.value.errors + "playlist ${request.errorMessage} "
                        _state.update {
                            state.value.copy(errors = errors, playlistIsLoading = false)
                        }

                    }
                    is RequestResult.Loading ->{
                        _state.update {
                            state.value.copy(playlistIsLoading = request.isLoading)
                        }

                    }
                }
            }

        }
    }

    private fun getUser(){
        viewModelScope.launch {
            userUseCases.getUserUseCase(
                username = null
            ).collectLatest {
                        request ->
                    when(request){
                        is RequestResult.Loading -> {
                            _state.update {
                                state.value.copy(userIsLoading = request.isLoading)
                            }
                        }
                        is RequestResult.Success ->{
                            val user = request.resource
                            _state.update {
                                state.value.copy(loggedInUser = user, userIsLoading = false)
                            }
                        }
                        is RequestResult.Error ->{
                            val errors = state.value.errors + request.errorMessage
                            _state.update {
                                state.value.copy(errors = errors, loggedInUser = null, userIsLoading = false)
                            }
                        }
                    }
                }
        }
    }

    fun handleChangeEvent(events: HomeScreenEvents){
        when(events){
            is HomeScreenEvents.RemoveShownMessage ->{
                removeShownMessage()
            }
            else ->{

            }
        }

    }
//    fun miniPlayerAction(action:PlayAction){
//        when(action){
//            PlayAction.FORWARD ->{
//                forwardMusic()
//            }
//            PlayAction.RESUME ->{
//                resumePlaying()
//            }
//            PlayAction.NEXT ->{
//                nextMusic()
//            }
//            PlayAction.PAUSE ->{
//                println("I have paused")
//                onPausedMusic()
//            }
//            PlayAction.PREVIOUS ->{
//                previousMusic()
//            }
//            PlayAction.REWIND ->{
//                rewindMusic()
//            }
//            PlayAction.STOP ->{
//                onStopPlaying()
//            }
//            else -> {
//
//            }
//        }
//
//
//    }


    private fun onPlayClicked(selectedLectureId: String){
        viewModelScope.launch {

        }

    }

    private fun removeShownMessage() {
        val errors = _state.value.errors.removeElement(0)
        _state.update {
            it.copy(errors = errors)
        }
    }

    private fun onStopPlaying(){
       /// musicInterface.stopMusic()
    }
    private fun onPausedMusic(){
    //    musicInterface.pauseMusic()
    }
    private fun resumePlaying(){
        viewModelScope.launch {
      //      musicInterface.resumeMusic()
        }
    }
    private fun rewindMusic(){
        viewModelScope.launch {
        //    musicInterface.skipBackward()
        }
    }
    private  fun forwardMusic(){
        viewModelScope.launch {
          //  musicInterface.fastForward()
        }
    }


    private fun removeFromFavourite(selectedLectureId: String){

    }




}