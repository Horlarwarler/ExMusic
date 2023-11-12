package com.crezent.domain

import com.crezent.common.util.RequestResult
import com.crezent.network.api.BaseApi
import kotlinx.coroutines.flow.Flow

class AuthenticationUseCase (
    private val baseApi: BaseApi
) {
    operator fun invoke():Flow<RequestResult<Unit>> {
        return baseApi.authenticate()
    }
}