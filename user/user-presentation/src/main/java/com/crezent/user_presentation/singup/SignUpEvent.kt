package com.crezent.user_presentation.singup

import android.net.Uri

sealed class SignUpEvent {
    class UsernameChange(val username: String) : SignUpEvent()
    class NameChange(val name: String) : SignUpEvent()
    class EmailChange(val emailAddress: String) : SignUpEvent()
    class PasswordChange(val password: String) : SignUpEvent()
    class ConfirmPasswordChange(val password: String) : SignUpEvent()
    class DisplayNameChange(val displayName: String) : SignUpEvent()
    class OnProfileImageSelected(val uri: Uri?, val byteArray: ByteArray?) : SignUpEvent()
    object ToggleRegisteredAsArtist: SignUpEvent()
    object SignUp:SignUpEvent()
    class CheckValueField(val fieldIndex:Int): SignUpEvent()

    object RemoveShownMessage:SignUpEvent()



}