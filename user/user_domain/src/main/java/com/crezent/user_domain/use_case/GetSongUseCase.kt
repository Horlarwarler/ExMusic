package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import kotlinx.coroutines.flow.Flow

class GetSongUseCase(
    private val exMusicRepo: ExMusicRepo
) {
    operator fun invoke(
        searchQuery:String,
        artistUsername:String?
    ): Flow<RequestResult<List<com.crezent.models.Song>>> {
        return exMusicRepo.getSongs(
            searchQuery = searchQuery,
            artistUsername = artistUsername
        )
    }
}