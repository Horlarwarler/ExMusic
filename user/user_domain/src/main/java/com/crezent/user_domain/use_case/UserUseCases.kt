package com.crezent.user_domain.use_case

// Putting Everything in a use case
data class UserUseCases(
    val signInUseCase: SignInUseCase,
    val signUpUseCase: SignUpUseCase,
    val profileUpdateUseCase: ProfileUpdateUseCase,
    val getUserUseCase: GetUserUseCase,
    val getPersonalPlaylistUseCase: GetPersonalPlaylistUseCase,
    val deleteSongUseCase: DeleteSongUseCase,
    val getArtistPlaylistUseCase: GetArtistPlaylistUseCase,
    val getTopPlaylistUseCase: GetTopPlaylistUseCase,
    val getSongUseCase: GetSongUseCase,
    val getArtistUseCase:GetArtistUseCase,
    val personalPlaylistActionUseCase: PersonalPlaylistActionUseCase,
    val realtimeActionUseCase: RealtimeActionUseCase,
    val recentPlayedUseCase: RecentPlayedUseCase
)
