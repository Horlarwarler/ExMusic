package com.example.talimlectures.domain.internet

sealed class NetworkState{
    object isConnected: NetworkState()
    object isDisconnected: NetworkState()
}
