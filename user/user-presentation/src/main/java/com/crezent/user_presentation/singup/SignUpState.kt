package com.crezent.user_presentation.singup

import android.net.Uri

data class SignUpState(
    val username:String ="",
    val usernameError: List<String> = emptyList(),
    val password:String ="",
    val passwordError: List<String> = emptyList(),
    val confirmPassword: String = "",
    val confirmPasswordError: List<String> = emptyList(),
    val name:String = "",
    val nameError: List<String> = emptyList(),

    val emailAddress:String= "",
    val emailAddressError: List<String> = emptyList(),

    val displayName:String= "",
    val displayNameError:List<String> = emptyList(),
    val registeredAsArtist:Boolean= false,
    val characterComplete:Boolean = false,
    val hasSpecialCharacter:Boolean = false,
    val passwordStrength:Int? = null,
    val profilePicture:Uri? = null,
    val isLoading:Boolean = false,
    val signUpSuccessful:Boolean = false,
    val errors:List<String> = emptyList(),
    val continueButtonEnabled:Boolean = false,
    val profileByteArray:ByteArray? = null
)
