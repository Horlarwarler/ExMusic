package com.crezent.user_domain.di

import com.crezent.common.preference.Preference
import com.crezent.data.repository.ExMusicRepo
import com.crezent.network.api.BaseApi
import com.crezent.user_domain.use_case.DeleteSongUseCase
import com.crezent.user_domain.use_case.GetArtistPlaylistUseCase
import com.crezent.user_domain.use_case.GetArtistUseCase
import com.crezent.user_domain.use_case.GetPersonalPlaylistUseCase
import com.crezent.user_domain.use_case.GetSongUseCase
import com.crezent.user_domain.use_case.GetTopPlaylistUseCase
import com.crezent.user_domain.use_case.GetUserUseCase
import com.crezent.user_domain.use_case.PersonalPlaylistActionUseCase
import com.crezent.user_domain.use_case.ProfileUpdateUseCase
import com.crezent.user_domain.use_case.RealtimeActionUseCase
import com.crezent.user_domain.use_case.RecentPlayedUseCase
import com.crezent.user_domain.use_case.SignInUseCase
import com.crezent.user_domain.use_case.SignUpUseCase
import com.crezent.user_domain.use_case.UserUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvideUserUseCase {

    @Singleton
    @Provides
    fun provideUserUseCase(
        preference: Preference,
        baseApi: BaseApi,
        exMusicRepo: ExMusicRepo
    ): UserUseCases{
        val signUpUseCase = SignUpUseCase(
            exMusicRepo = exMusicRepo,
            baseApi = baseApi,
            preference = preference )
        val signInUseCase =  SignInUseCase(preference = preference, baseApi = baseApi)
        val profileUpdateUseCase = ProfileUpdateUseCase(baseApi = baseApi, exMusicRepo = exMusicRepo)
        val getUserUseCase = GetUserUseCase(exMusicRepo)
        val getPersonalPlaylistUseCase = GetPersonalPlaylistUseCase(exMusicRepo)
        val deleteSongUseCase = DeleteSongUseCase(exMusicRepo)
        val getArtistPlaylistUseCase = GetArtistPlaylistUseCase(exMusicRepo)
        val getTopPlaylistUseCase = GetTopPlaylistUseCase(exMusicRepo)
        val getSongUseCase = GetSongUseCase(exMusicRepo)
        val  getArtistUseCase = GetArtistUseCase(exMusicRepo)
        val personalPlaylistActionUseCase = PersonalPlaylistActionUseCase(exMusicRepo)
        val realtimeActionUseCase = RealtimeActionUseCase(baseApi)
        val recentPlayedUseCase = RecentPlayedUseCase(exMusicRepo)
        return UserUseCases(
            signInUseCase = signInUseCase,
            signUpUseCase = signUpUseCase,
            profileUpdateUseCase = profileUpdateUseCase,
            getUserUseCase = getUserUseCase,
            getPersonalPlaylistUseCase = getPersonalPlaylistUseCase,
            deleteSongUseCase = deleteSongUseCase,
            getArtistPlaylistUseCase = getArtistPlaylistUseCase,
            getTopPlaylistUseCase = getTopPlaylistUseCase,
            getSongUseCase = getSongUseCase,
            getArtistUseCase = getArtistUseCase,
            personalPlaylistActionUseCase = personalPlaylistActionUseCase,
            realtimeActionUseCase = realtimeActionUseCase,
            recentPlayedUseCase = recentPlayedUseCase
        )
    }
}