package com.crezent.di

import android.content.Context
import com.crezent.download.DownloadServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvideDownloadRepo {
    @Provides
    @Singleton
    fun provideDownloadRepo(
        @ApplicationContext context: Context
    ):DownloadServiceRepository {
        return  DownloadServiceRepository(context)
    }
}