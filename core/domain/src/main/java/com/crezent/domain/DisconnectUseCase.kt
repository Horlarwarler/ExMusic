package com.crezent.domain

import com.crezent.network.api.BaseApi

class DisconnectUseCase (
    private val baseApi: BaseApi
) {
    suspend operator  fun invoke(){
        baseApi.disconnect()
    }
}