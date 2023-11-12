package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import com.crezent.models.User
import kotlinx.coroutines.flow.Flow

class GetArtistUseCase (
    private val exMusicRepo: ExMusicRepo
) {
    operator fun invoke(
    ): Flow<RequestResult<List<User>>> {
        return exMusicRepo.getArtists()
    }
}