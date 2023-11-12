package com.crezent.user_presentation.profile

 sealed class ProfileEvent {
     class UpdatePlaylistStatus(val status: PlaylistStatus): ProfileEvent()
     object RemoveShownMessage:ProfileEvent()
}