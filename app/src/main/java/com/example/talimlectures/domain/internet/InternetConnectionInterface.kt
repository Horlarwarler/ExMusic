package com.example.talimlectures.domain.internet

interface InternetConnectionInterface {
    fun getNetworkState()
    var networkState: NetworkConnectionState
}