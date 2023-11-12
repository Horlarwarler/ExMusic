package com.crezent.network.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto<T>(
    val data: T
)