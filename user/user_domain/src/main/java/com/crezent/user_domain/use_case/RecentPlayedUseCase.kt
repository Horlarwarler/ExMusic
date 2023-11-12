package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import kotlinx.coroutines.flow.Flow

class RecentPlayedUseCase(
    private val exMusicRepo: ExMusicRepo
) {
    operator fun invoke(): Flow<RequestResult<List<com.crezent.models.RecentlyPlayed>>> {
        return exMusicRepo.getAllRecentlyPlayed()
    }

}