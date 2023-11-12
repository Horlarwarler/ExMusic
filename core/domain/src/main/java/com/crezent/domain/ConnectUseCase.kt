package com.crezent.domain

import com.crezent.network.api.BaseApi

class ConnectUseCase (
    private val baseApi: BaseApi
) {
    suspend operator  fun invoke(){

        baseApi.connect()
    }
}