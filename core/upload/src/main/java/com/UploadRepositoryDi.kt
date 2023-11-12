package com

import android.content.Context
import com.crezent.network.api.CreatorApi
import com.upload.UploadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UploadRepositoryDi {

    @Singleton
    @Provides
    fun providesUploadRepository(
        @ApplicationContext context: Context,
        creatorApi: CreatorApi
    ): UploadRepository{
        return  UploadRepository(
            context = context,
            creatorApi = creatorApi
        )
    }
}