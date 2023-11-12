package com.crezent.database.di

import com.crezent.database.ExMusicDatabase
import com.crezent.database.dao.ArtistPlaylistDao
import com.crezent.database.dao.PlaylistDao
import com.crezent.database.dao.RecentDao
import com.crezent.database.dao.SongDao
import com.crezent.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesUserDao(
        exMusicDatabase: ExMusicDatabase
    ):UserDao = exMusicDatabase.userDao
    @Provides
    fun providesPlaylistDao(
        exMusicDatabase: ExMusicDatabase
    ):PlaylistDao = exMusicDatabase.playlistDao

    @Provides
    fun providesArtistPlaylistDao(
        exMusicDatabase: ExMusicDatabase
    ):ArtistPlaylistDao = exMusicDatabase.artistPlaylistDao


    @Provides
    fun providesRecentDao(
        exMusicDatabase: ExMusicDatabase
    ):RecentDao = exMusicDatabase.recentDao
    @Provides
    fun providesSongDao(
        exMusicDatabase: ExMusicDatabase
    ):SongDao = exMusicDatabase.songDao

}