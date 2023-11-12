package com.crezent.user_presentation.profile_update

data class ProfileUpdateState(
    val user: com.crezent.models.User? = null,
    val username:String?=null,
    val password:String?=null,
    val passwordError:List<String> = emptyList(),
    val emailAddress:String?=null,
    val emailAddressError:List<String> = emptyList(),
    val displayName:String?=null,
    val registeredAsArtist:Boolean= false,
    val characterComplete:Boolean = false,
    val hasSpecialCharacter:Boolean = false,
    val passwordStrength:Int? = null,
    val isLoading:Boolean = false,
    val updateSuccessful:Boolean = false,
    val errors:List<String> = emptyList(),
    val loginButtonEnabled:Boolean = true,
    val userRemovedPicture:Boolean = false,
    val profilePicture:ByteArray? = null,
    )
