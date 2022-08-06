package com.example.talimlectures.domain.internet

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class NetworkConnectionState(
    val isConnected: SharedFlow<Boolean> = MutableSharedFlow()
)
