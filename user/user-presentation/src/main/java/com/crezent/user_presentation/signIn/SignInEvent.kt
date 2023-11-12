package com.crezent.user_presentation.signIn


sealed class SignInEvent {
    class EmailAddressChange(val username: String) : SignInEvent()
    class PasswordChange(val password: String) : SignInEvent()

    object SignIn:SignInEvent()
    object RemoveShownMessage:SignInEvent()

}