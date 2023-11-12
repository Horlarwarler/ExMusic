package com.crezent.domain

import com.crezent.network.api.BaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationUseCasesDi {
    @Provides
    @Singleton
    fun providesApplicationUseCases(
        api: BaseApi
    ):ApplicationUseCases {
        val authenticationUseCase = AuthenticationUseCase(api)
        val connectUseCase = ConnectUseCase(api)
        val disconnectUseCase  = DisconnectUseCase(api)
        return ApplicationUseCases(
            authenticationUseCase = authenticationUseCase,
            connectUseCase = connectUseCase,
            disconnectUseCase = disconnectUseCase
        )
    }
}