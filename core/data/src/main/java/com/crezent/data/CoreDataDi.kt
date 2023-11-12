package com.crezent.data

import android.content.Context
import com.crezent.common.preference.Preference
import com.crezent.data.repository.ExMusicRepo
import com.crezent.data.repository.ExMusicRepoImpl
import com.crezent.data.util.AuthenticationManager
import com.crezent.data.util.internet.NetworkMonitorImpl
import com.crezent.data.util.internet.NetworkMonitorInterface
import com.crezent.database.dao.ArtistPlaylistDao
import com.crezent.database.dao.PlaylistDao
import com.crezent.database.dao.RecentDao
import com.crezent.database.dao.SongDao
import com.crezent.database.dao.UserDao
import com.crezent.network.api.BaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreDataDi {

    @Provides
    @Singleton
    fun providesAuthenticationManager(
        baseApi: BaseApi
    ): AuthenticationManager = AuthenticationManager(baseApi)
    @Provides
    @Singleton
    fun providesExMusicRepo(
        userDao: UserDao,
        songDao: SongDao,
        baseApi: BaseApi,
        preference: Preference,
        playlistDao: PlaylistDao,
        artistPlaylistDao: ArtistPlaylistDao,
        recentDao: RecentDao
    ): ExMusicRepo {
        return  ExMusicRepoImpl(
            userDao, songDao, baseApi, preference, playlistDao, artistPlaylistDao, recentDao
        )
    }
    @Provides
    @Singleton
    fun providesNetworkMonitor(
        @ApplicationContext context:Context
    ):NetworkMonitorInterface {
        return  NetworkMonitorImpl(
            context = context
        )
    }
}