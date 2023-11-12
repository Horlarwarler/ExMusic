package com.crezent.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crezent.database.ExMusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context:Context
    ):ExMusicDatabase =
        Room.databaseBuilder(
            context = context,
            klass = ExMusicDatabase::class.java,
            name = "ExMusic"
        ).build()
}