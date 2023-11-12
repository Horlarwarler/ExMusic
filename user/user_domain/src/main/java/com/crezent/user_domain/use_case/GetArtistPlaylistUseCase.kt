package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo
import kotlinx.coroutines.flow.Flow

class GetArtistPlaylistUseCase ( private val exMusicRepo: ExMusicRepo) {
    operator fun invoke(
        artistUsername:String
    ): Flow<RequestResult<List<com.crezent.models.ArtistPlaylist>>> {
        return exMusicRepo.getArtistPlaylist(artistUsername)
    }
}