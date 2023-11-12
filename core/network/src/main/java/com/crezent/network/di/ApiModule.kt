package com.crezent.network.di

import com.crezent.network.api.BaseApi
import com.crezent.network.api.CreatorApi
import com.crezent.network.api.KtorBaseApi
import com.crezent.network.api.KtorCreatorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideKtorApi(
        client:HttpClient
    ): BaseApi {
        return KtorBaseApi(client)
    }
    @Provides
    @Singleton

    fun provideKtorCreatorApi(
        client:HttpClient
    ):CreatorApi {
        return  KtorCreatorApi(client)
    }
}