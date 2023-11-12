package com.crezent.user_presentation.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.common.util.LinkedList
import com.crezent.common.util.RequestResult
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
class SignInScreenModel @Inject constructor(
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private  val errorLinkedList = LinkedList<String>()



    fun handleUiEvent(event:SignInEvent){
        when(event){


            is SignInEvent.PasswordChange -> {
                updatePassword(event.password)
            }
            is SignInEvent.EmailAddressChange ->{
                updateUsername(event.username)
            }

            is SignInEvent.SignIn -> {
                signIn()
            }
            is SignInEvent.RemoveShownMessage ->{
                removeShownMessage()
            }

        }
    }


    private fun signIn() {

        val password =_state.value.password
        val username =_state.value.emailAddress

        viewModelScope.launch(Dispatchers.IO) {
            userUseCases.signInUseCase.invoke(
                username = username,
                password = password
            ).collectLatest {
                result ->
                when(result) {

                    is RequestResult.Loading ->{
                        _state.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is RequestResult.Success ->{
                        _state.update {
                            it.copy(signInSuccessful = true, isLoading = false)
                        }
                    }
                    is RequestResult.Error ->{
                        val errorMessage = result.errorMessage
                        errorLinkedList.append(errorMessage)

                        _state.update {
                            it.copy(signInSuccessful = false, isLoading = false, error = errorLinkedList.getHead())
                        }
                    }
                }
            }

        }

    }



    private fun updateUsername(username: String) {
      _state.update {
          it.copy(emailAddress = username)
      }
        enableLoginButton()
    }



    private fun updatePassword(password: String) {

        _state.update {
            it.copy(password = password)
        }
        enableLoginButton()

    }

    private fun enableLoginButton(){
        val usernameValid =_state.value.emailAddress?.isNotEmpty()  == true
        val passwordValid =_state.value.password?.isNotEmpty()  == true
        val loginButtonEnabled = usernameValid && passwordValid
        _state.update {
            it.copy(loginButtonEnabled = loginButtonEnabled)
        }
    }

    private fun removeShownMessage() {
        errorLinkedList.removeFirst()
        _state.update {
            it.copy(
                error = errorLinkedList.getHead()
            )
        }

    }

}