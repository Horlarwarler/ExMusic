package com.crezent.network.models

import kotlinx.serialization.Serializable

@Serializable
data class JwtToken(
    val token:String
)

