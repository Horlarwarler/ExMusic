package com.crezent.domain

data class ApplicationUseCases(
    val authenticationUseCase: AuthenticationUseCase,
    val connectUseCase: ConnectUseCase,
    val disconnectUseCase: DisconnectUseCase
)
