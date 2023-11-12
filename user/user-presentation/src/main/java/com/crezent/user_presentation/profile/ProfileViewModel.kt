package com.crezent.user_presentation.profile

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.preference.Preference
import com.crezent.common.util.RequestResult
import com.crezent.common.util.removeElement
import com.crezent.user_domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val preference: Preference,
    private val userUseCase: UserUseCases,
    private val downloadServiceRepository: com.crezent.download.DownloadServiceRepository,
) : ViewModel(){

    private var _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val downloadHashMap :HashMap<String, com.crezent.models.FileResult> = hashMapOf()

    private val loggedInUsername =  preference.loggedInUsername()
    private val argumentUsername:String =  savedStateHandle.get<String>("username")!!
    private val isCurrentUser = loggedInUsername == argumentUsername


    init {
        getProfile()
        getAllCurrentDownload()
    }

    private fun getAllCurrentDownload(){
        downloadServiceRepository.currentDownloads.onEach { downloads ->
            //Each Emission get
            val playlists = _state.value.playlists.map {
                    playlist ->
                val status =  downloads.find {
                    download ->
                    download.songId == playlist.songId
                }
                status?.let {
                    playlist.copy( taskStatus = it.taskStatus, downloadProgress = it.progress)
                }
                playlist

            }
            _state.update {
                it.copy(playlists = playlists)
            }

        }.launchIn(viewModelScope)
    }
    fun handleUiEvent(event: ProfileEvent){

        when(event){

            is ProfileEvent.UpdatePlaylistStatus ->{
                updatePlaylistStatus(event.status)
            }

            is ProfileEvent.RemoveShownMessage ->{
                removeShownMessage()
            }

        }
    }

    private fun getProfile(){
        //Get the profile of the user
        viewModelScope.launch {

            userUseCase.getUserUseCase(
                username = if (isCurrentUser)null else argumentUsername,
                cacheUser = isCurrentUser)
                .collectLatest {
                        result ->

                    when(result){
                        is RequestResult.Loading ->{
                            _state.update {
                                state.value.copy( isLoading = true)
                            }
                        }
                        is RequestResult.Success ->{
                            val user = result.resource
                            Log.d("User"," Followers ${user.followers.size} Followings = ${user.following.size}")
                            _state.update {
                                state.value.copy( user= user,isLoading = false, isCurrentUser =isCurrentUser )
                            }
                            if (isCurrentUser){
                                getPersonalPlaylist()
                            }

                        }
                        is RequestResult.Error ->{
                            val errors = state.value.errors + result.errorMessage
                            _state.update {
                                state.value.copy(errors = errors, isLoading = false)
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun getPersonalPlaylist(){
        viewModelScope.launch {
            userUseCase.getPersonalPlaylistUseCase().collectLatest {
                        playlistRequest ->

                    when(playlistRequest){
                        is RequestResult.Loading ->{
                            _state.update {
                                state.value.copy( isLoading = true)
                            }
                        }
                        is RequestResult.Success ->{
                            val playlist = playlistRequest.resource
                            _state.update {
                                state.value.copy( playlists = playlist,isLoading = false)
                            }

                        }
                        is RequestResult.Error ->{
                            val errors = state.value.errors + playlistRequest.errorMessage
                            _state.update {
                                state.value.copy(errors = errors, isLoading = false)
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun updatePlaylistStatus(playlistStatus: PlaylistStatus){
//        val downloadHashmap  = state.value.downloadHashMap
//        val songId = playlistStatus.songId
//        val downloadStatus= playlistStatus.fileStatus
//        val progress = playlistStatus.progress
//        if (!downloadHashmap.containsKey(songId)){
//            return
//        }
//        val song = downloadHashmap[songId]!!
//        val updateSong = song.copy(
//            fileStatus = downloadStatus,
//            downloadProgress = progress
//        )
//        downloadHashmap.replace(songId, updateSong)
//        _state.update {
//            state.value.copy(downloadHashMap = downloadHashmap)
//        }
    }
    //


    private fun removeShownMessage(){
        val errors = _state.value.errors.removeElement(0)
        _state.update {
            it.copy(errors = errors)
        }
    }
}