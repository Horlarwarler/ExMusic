package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import kotlinx.coroutines.flow.Flow

class GetPersonalPlaylistUseCase (
    private val exMusicRepo: ExMusicRepo
) {
    operator fun invoke():Flow<RequestResult<List<com.crezent.models.PersonalPlaylist>>> {
        return  exMusicRepo.getPersonalPlaylist()
    }
}