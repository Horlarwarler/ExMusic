package com.crezent.user_presentation.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.RequestResult
import com.crezent.common.util.UiEvent
import com.crezent.common.util.removeElement
import com.crezent.user_domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicViewModel  @Inject constructor(
    private val userUseCases: UserUseCases
):ViewModel() {

    private var _state = MutableStateFlow(MusicScreenState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    init {
        getArtists()
        getSongs()
        getTopPlaylist()
    }


    private fun authorizedUser(){
    }

    private fun getTopPlaylist(){
        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.getTopPlaylistUseCase().collect{
                    request ->
                when(request){
                    is RequestResult.Success ->{
                        val playlists = request.resource

                        _state.update {
                            state.value.copy(playlists = playlists, playlistIsLoading = false)
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

    private fun getSongs(){
        val searchQuery = state.value.searchQuery
        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.getSongUseCase(
                artistUsername = null,
                searchQuery = searchQuery
            ).collect{
                    request ->
                when(request){
                    is RequestResult.Success ->{
                        val songs = request.resource
                        _state.update {
                            state.value.copy(songs = songs, songIsLoading = false)
                        }
                    }
                    is RequestResult.Error -> {
                        val errors = state.value.errors + "Music ${request.errorMessage} "
                        _state.update {
                            state.value.copy(errors = errors, songIsLoading = false)
                        }

                    }
                    is RequestResult.Loading ->{
                        _state.update {
                            state.value.copy(songIsLoading = request.isLoading)
                        }

                    }
                }
            }

        }
    }

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
                        val artists = request.resource

                        _state.update {
                            state.value.copy(artists = artists)
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

    fun handleUiEvent(event: MusicEvent){

        when(event){

            is MusicEvent.PlaylistAction ->{
                playlistAction(event.song)
            }
            is MusicEvent.RemoveShownMessage ->{
                removeShownMessage()
            }
            is MusicEvent.SelectSong ->{

            }

            is MusicEvent.RemoveFromFavourite -> {

            }

            is MusicEvent.AddToFavourite ->{

            }

        }

    }

    private fun playlistAction(song: com.crezent.models.Song){
        viewModelScope.launch {
            val personalPlaylist = com.crezent.models.PersonalPlaylist(
                songId = song.songId,
                name = song.title,
                thumbnail = song.thumbnailUrl,
                artistName = song.artistUsername,
                taskStatus = com.crezent.models.TaskStatus.NONE,
                isDownloaded = song.isDownloaded,
                songUrl = song.audioUrl
            )
            userUseCases.personalPlaylistActionUseCase.invoke(playlist = personalPlaylist)
        }
    }

    private fun removeShownMessage() {
        val errors = _state.value.errors.removeElement(0)
        _state.update {
            it.copy(errors = errors)
        }
    }
}