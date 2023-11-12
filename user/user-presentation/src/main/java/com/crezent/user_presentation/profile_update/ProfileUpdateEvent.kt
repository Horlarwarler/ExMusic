package com.crezent.user_presentation.profile_update

sealed class ProfileUpdateEvent {
    class UsernameChange(val username: String) : ProfileUpdateEvent()
    class EmailChange(val emailAddress: String) : ProfileUpdateEvent()
    class PasswordChange(val password: String) : ProfileUpdateEvent()
    class DisplayNameChange(val displayName: String) : ProfileUpdateEvent()
    class OnProfileImageSelected(val byteArray: ByteArray? ) : ProfileUpdateEvent()
    object ToggleRegisteredAsArtist: ProfileUpdateEvent()
    object Update:ProfileUpdateEvent()

    object RemoveShownMessage:ProfileUpdateEvent()
    object Refresh:ProfileUpdateEvent()

    object CheckEmailAddressField: ProfileUpdateEvent()
}