package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import com.crezent.models.ArtistPlaylist
import kotlinx.coroutines.flow.Flow



class GetTopPlaylistUseCase(
    private val exMusicRepo: ExMusicRepo
) {
    operator fun invoke(
    ): Flow<RequestResult<List<ArtistPlaylist>>> {
        return exMusicRepo.getArtistPlaylist(null)
    }
}