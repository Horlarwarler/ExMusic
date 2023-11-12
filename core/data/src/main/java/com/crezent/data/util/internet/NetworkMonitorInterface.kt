package com.crezent.data.util.internet

import kotlinx.coroutines.flow.Flow

interface NetworkMonitorInterface {
    fun observeNetwork(): Flow<NetworkState>
}