package com.crezent.user_presentation.profile_update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.FileHelper
import com.crezent.common.util.RequestResult
import com.crezent.common.util.nullIfTheSame
import com.crezent.common.util.removeElement
import com.crezent.user_domain.use_case.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val userUseCases: UserUseCases,
    private val fileHelper: FileHelper
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUpdateState())
    val state = _state.asStateFlow()


    init {
        getUser()
    }

    fun handleUiEvent(event:ProfileUpdateEvent){
        when(event){

            is ProfileUpdateEvent.DisplayNameChange ->{
                updateDisplayName(event.displayName)
            }
            is ProfileUpdateEvent.PasswordChange -> {
                updatePassword(event.password)
            }
            is ProfileUpdateEvent.UsernameChange ->{
                updateUsername(event.username)
            }
            is ProfileUpdateEvent.EmailChange  ->{
                updateEmail(event.emailAddress)
            }
            is ProfileUpdateEvent.OnProfileImageSelected ->{
                updateProfilePicture(event.byteArray)
            }
            is ProfileUpdateEvent.ToggleRegisteredAsArtist ->{

            }
            is ProfileUpdateEvent.RemoveShownMessage ->{
                removeShownMessage()
            }

            is ProfileUpdateEvent.Update -> {
                updateUser()
            }

            is ProfileUpdateEvent.Refresh ->{
                getUser()
            }

            ProfileUpdateEvent.CheckEmailAddressField -> {

            }
        }
    }

    private fun getUser(){
        viewModelScope.launch {
            userUseCases.getUserUseCase(
                username = null,
                cacheUser = true)
                .collectLatest {
                        request ->
                    when(request){
                        is RequestResult.Loading -> {
                            _state.update {
                                state.value.copy(isLoading = request.isLoading)
                            }
                        }
                        is RequestResult.Success ->{
                            val user = request.resource
                            val username = user.username
                            val password = null
                            val emailAddress = user.emailAddress
                            val displayName = user.displayName
                            _state.update {
                                state.value.copy(
                                    user = user,
                                    username = null,
                                    password = null,
                                    emailAddress =  null,
                                    displayName = null,
                                    isLoading = false
                                )
                            }
                        }
                        is RequestResult.Error ->{
                            val errors = state.value.errors + request.errorMessage
                            _state.update {
                                state.value.copy(errors = errors, user = null, isLoading = false)
                            }
                        }
                    }
                }
        }
    }


    private fun removeShownMessage() {
        val errors = _state.value.errors.removeElement(0)
        _state.update {
            state.value.copy(errors = errors)
        }
    }

    private fun updateProfilePicture(profileArray: ByteArray?) {
        val userRemovedPicture = profileArray == null
        _state.update {
            state.value.copy(profilePicture = profileArray, userRemovedPicture = userRemovedPicture )
        }
    }

    private fun updateUser() {
        val user = state.value.user!!
        val inputEmailAddress =_state.value.emailAddress
        val inputPassword =_state.value.password
        val inputDisplayName =_state.value.displayName



        val emailAddress = user.emailAddress.nullIfTheSame(inputEmailAddress)
        val password = user.password.nullIfTheSame(inputPassword)
        val displayName = user.displayName.nullIfTheSame(inputDisplayName)
        val isArtist = _state.value.registeredAsArtist
        val username =user.username
        val profilePictureRemove = if ( state.value.userRemovedPicture)"true" else "false"
        val profilePicture = state.value.profilePicture


        viewModelScope.launch {
            userUseCases.profileUpdateUseCase(
                username = username,
                password = password,
                displayName = displayName,
                emailAddress = emailAddress,
                profilePicture = profilePicture,
                profilePictureRemove = profilePictureRemove
            ).collectLatest {
                request ->
                when(request) {

                        is RequestResult.Loading ->{
                            _state.update {
                                state.value.copy(isLoading = request.isLoading)
                            }
                        }
                        is RequestResult.Success ->{

                            val updatedUser = request.resource
                            val updatedEmailAddress = updatedUser.emailAddress
                            val updatedDisplayName = updatedUser.displayName
                            val updatedProfilePicture = updatedUser.profilePicture
                            _state.update {
                                state.value.copy(
                                    updateSuccessful = true,
                                    isLoading = false,
                                    user = updatedUser,
                                    emailAddress = updatedEmailAddress,
                                    displayName = updatedDisplayName,
                                    userRemovedPicture = false
                                )
                            }
                        }
                        is RequestResult.Error ->{
                            val errorMessage = request.errorMessage
                            val errors = _state.value.errors + errorMessage
                            _state.update {
                                state.value.copy(updateSuccessful = false,errors = errors, isLoading = false)
                            }
                        }
                    }
            }
        }

    }


    private fun updateEmail(emailAddress: String) {
       _state.update {
           it.copy(emailAddress = emailAddress)
       }
        enableContinueButton()
    }

    private fun updateUsername(username: String) {
      _state.update {
          it.copy(username = username)
      }
        enableContinueButton()
    }


    private fun updateDisplayName(displayName: String) {
        _state.update {
            it.copy(displayName = displayName)
        }
        enableContinueButton()
    }

    private fun updatePassword(password: String) {
        val samePassword = state.value.password.isNullOrEmpty()

        val characterComplete =password.length >= 7
        val regex = Regex("[^a-zA-Z0-9 ]")
        val hasSpecialCharacter =regex.find(password) != null

        _state.update {
            state.value.copy(password = password, characterComplete = characterComplete, hasSpecialCharacter = hasSpecialCharacter)
        }
        enableContinueButton()

    }

    private fun enableContinueButton(){
        val samePassword = state.value.password.isNullOrEmpty()

        val characterComplete = samePassword || state.value.characterComplete
        val hasSpecialCharacter = samePassword|| state.value.hasSpecialCharacter
        val emailValid  = state.value.username?.isNotEmpty() == true
        val continueButtonEnabled = characterComplete && hasSpecialCharacter && emailValid
        _state.update {
            state.value.copy(loginButtonEnabled = continueButtonEnabled)
        }
    }

}