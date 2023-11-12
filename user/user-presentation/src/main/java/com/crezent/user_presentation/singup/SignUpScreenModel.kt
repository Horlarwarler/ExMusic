package com.crezent.user_presentation.singup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.FileHelper
import com.crezent.common.util.RequestResult
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
class SignUpScreenModel @Inject constructor(
    private val fileHelper: FileHelper,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()


    fun handleUiEvent(event:SignUpEvent){
        when(event){

            is SignUpEvent.DisplayNameChange ->{
                updateDisplayName(event.displayName)
            }
            is SignUpEvent.PasswordChange -> {
                updatePassword(event.password)
            }
            is SignUpEvent.ConfirmPasswordChange -> {
                updateConfirmPassword(event.password)
            }
            is SignUpEvent.UsernameChange ->{
                updateUsername(event.username)
            }
            is SignUpEvent.NameChange ->{
                updateName(event.name)
            }
            is SignUpEvent.EmailChange  ->{
                updateEmail(event.emailAddress)
            }
            is SignUpEvent.OnProfileImageSelected ->{

                updateProfilePicture(event.uri, event.byteArray)
            }
            is SignUpEvent.ToggleRegisteredAsArtist ->{

            }
            is SignUpEvent.RemoveShownMessage ->{
                removeShownMessage()
            }

            is SignUpEvent.SignUp -> {
                signUp()
            }
            is SignUpEvent.CheckValueField -> {
                checkValueField(event.fieldIndex)
            }

        }
    }

    private fun removeShownMessage() {
        val errors = _state.value.errors.removeElement(0)
        _state.update {
            it.copy(errors = errors)
        }
    }

    private fun updateProfilePicture(uri: Uri?, byteArray: ByteArray?) {
        try {
         //   fileHelper.verifyFile(uri,"image")
            _state.update {
                it.copy(profilePicture = uri , profileByteArray =byteArray )
            }
        }
        catch (error: com.crezent.common.util.CustomError){
            val errors = _state.value.errors + "error.errorMessage" //TODO
            _state.update {
                it.copy(errors = errors, profilePicture = null)
            }
        }
        catch (error:Exception){
            val errorMessage = error.message?:"Unknown Error"
            val errors = _state.value.errors + errorMessage
            _state.update {
                it.copy(errors = errors)
            }
        }

    }

    private fun signUp() {
        val emailAddress =_state.value.emailAddress?:return
        val password =_state.value.password?:return
        val displayName =_state.value.displayName?:return
        val username =_state.value.username?:return
        val isArtist = if (_state.value.registeredAsArtist) "true" else "false"
        val profilePicture:ByteArray? =getProfilePicture()

        viewModelScope.launch {
            userUseCases.signUpUseCase(
                username = username,
                password = password,
                emailAddress = emailAddress,
                displayName = displayName,
                asArtist = isArtist,
                profilePicture = profilePicture
            ).collectLatest {

                request ->
                when(request) {

                    is RequestResult.Loading ->{
                        _state.update {
                            it.copy(isLoading = request.isLoading)
                        }
                    }
                    is RequestResult.Success ->{
                        _state.update {
                            it.copy(signUpSuccessful = true,)
                        }
                    }
                    is RequestResult.Error ->{
                        val errorMessage = request.errorMessage
                        val errors = _state.value.errors + errorMessage
                        _state.update {
                            it.copy(signUpSuccessful = false,errors = errors)
                        }
                    }
                }
            }
        }

    }

    private fun getProfilePicture():ByteArray?{

        val uri = _state.value.profilePicture?:return null
        return fileHelper.byteFromUri(uri)
    }
    private fun updateEmail(emailAddress: String) {
       _state.update {
           it.copy(emailAddress = emailAddress)
       }
        if (state.value.emailAddressError.isNotEmpty()){
            checkEmailField()
        }
        enableContinueButton()
    }

    private fun updateUsername(username: String) {

      _state.update {
          it.copy(username = username)
      }
        if (state.value.usernameError.isNotEmpty()){
           checkUsernameField()
        }
        enableContinueButton()
    }
    private fun updateName(name: String) {
        _state.update {
            it.copy(name = name)
        }
        if (state.value.nameError.isNotEmpty()){
            checkNameField()
        }
        enableContinueButton()
    }



    private fun updateDisplayName(displayName: String) {
        _state.update {
            it.copy(displayName = displayName)
        }
        if (state.value.displayNameError.isNotEmpty()){
            checkDisplayNameField()
        }
        enableContinueButton()
    }

    private fun updatePassword(password: String) {
        val characterComplete = password.length >= 7
        val regex = Regex("[^a-zA-Z0-9 ]")
        val hasSpecialCharacter = regex.find(password) != null
        val minimumSeven = "Minimum of seven character"
        val specialCharacter  = "Must Include Special Character"
        val passwordError = when {
            !hasSpecialCharacter && !characterComplete -> {
                listOf(minimumSeven, specialCharacter )
            }
            !hasSpecialCharacter ->  {
                listOf(specialCharacter)
            }
            !characterComplete -> {
                listOf(minimumSeven)
            }
            else -> {
                emptyList()
            }
        }

        _state.update {
            it.copy(
                password = password,
                characterComplete = characterComplete,
                passwordError = passwordError,
                hasSpecialCharacter = hasSpecialCharacter)
        }

        enableContinueButton()

    }

    private fun updateConfirmPassword(password: String){
        _state.update {
            it.copy(
                confirmPassword = password,
            )
        }
        checkPasswordMatch()
        enableContinueButton()
    }
    private fun checkPasswordMatch(){
        val passwordFieldValue = state.value.password
        val confirmPasswordField = state.value.confirmPassword
        val passwordMatch = passwordFieldValue == confirmPasswordField
        val confirmPasswordError = if (passwordMatch ) {
            emptyList()
        }
        else {
           listOf("Password does not match")
        }
        _state.update {
            it.copy(
                confirmPasswordError = confirmPasswordError
            )
        }
    }
    private fun checkValueField(index:Int){
        when(index){
            0 -> {
               checkNameField()
            }
            1 -> {
                checkUsernameField()

            }
            2 -> {
                checkDisplayNameField()

            }
            3 -> {
                checkEmailField()
            }
            else -> {

            }
        }


    }

    private fun checkNameField(){
        val nameEmpty = state.value.name.isEmpty()
        val error = if (nameEmpty) listOf("Name Cannot be empty") else emptyList()
        _state.update {
            it.copy(nameError = error)
        }

    }
    private fun checkUsernameField(){
        val usernameEmpty = state.value.username.isEmpty()
        val usernameError = if (usernameEmpty){
            listOf("Username Cannot be empty")
        }
        else {
            emptyList()
        }
        _state.update {
            it.copy(usernameError = usernameError)
        }
    }
    private fun checkDisplayNameField(){
        val valueEmpty = state.value.displayName.isEmpty()
        val valueError = if (valueEmpty){
            listOf("Display Cannot be empty")
        }
        else {
            emptyList()
        }
        _state.update {
            it.copy(displayNameError = valueError)
        }
    }
    private fun checkEmailField(){
        val valueEmpty = state.value.emailAddress.isEmpty()
        val valueError = if (valueEmpty){
             listOf("Email Cannot be empty")
        }
        else {
            emptyList()
        }
        _state.update {
            it.copy(emailAddressError = valueError)
        }
    }
    private fun enableContinueButton(){
      //  val characterComplete = _state.value.characterComplete
        val usernameValid  = state.value.usernameError.isEmpty() && state.value.username.isNotBlank()
        val passwordValid = state.value.passwordError.isEmpty()   && state.value.password.isNotBlank()
        val passwordMatch = state.value.confirmPasswordError.isEmpty() && state.value.confirmPassword.isNotBlank()
        val nameValid  = state.value.nameError.isEmpty() && state.value.name.isNotBlank()
        val displayNameValid  = state.value.displayNameError.isEmpty() && state.value.displayName.isNotBlank()
        val emailValid  = state.value.username.isNotEmpty()
        val continueButtonEnabled = passwordMatch &&  passwordValid && nameValid &&displayNameValid  && usernameValid && emailValid
        _state.update {
            it.copy(continueButtonEnabled = continueButtonEnabled)
        }
    }
}