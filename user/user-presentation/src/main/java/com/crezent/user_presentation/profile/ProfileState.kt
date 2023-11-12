package com.crezent.user_presentation.profile

data class ProfileState(
    val user: com.crezent.models.User? = null,
    val playlists:List<com.crezent.models.PersonalPlaylist> = emptyList(),
    val downloadHashMap: HashMap<String, com.crezent.models.FileResult> = hashMapOf(),
    val errors:List<String> = emptyList(),
    val isLoading:Boolean = false,
    val profileIsLoading:Boolean = true,
    val playlistIsLoading:Boolean = true,
    val isCurrentUser:Boolean = false,
)
