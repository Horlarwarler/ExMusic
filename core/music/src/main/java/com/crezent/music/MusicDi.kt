package com.crezent.music

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicDi {
    @Provides
    @Singleton
    fun providesPlayback(
        @ApplicationContext context:Context
    ): PlaybackRepository {
        return  PlaybackRepository(context)
    }
}