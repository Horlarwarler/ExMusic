package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val exMusicRepo: ExMusicRepo
) {

    operator fun invoke(
        username:String?,
        cacheUser:Boolean = false
    ):Flow<RequestResult<com.crezent.models.User>> {
        return exMusicRepo.getUser(
            username = username,
            cacheUser = cacheUser
        )

    }
}