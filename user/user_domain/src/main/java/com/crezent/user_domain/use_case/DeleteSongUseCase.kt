package com.crezent.user_domain.use_case

import com.crezent.common.util.RequestResult
import com.crezent.data.repository.ExMusicRepo

class DeleteSongUseCase(
    private val exMusicRepo: ExMusicRepo
) {
    suspend operator fun invoke(songId:String): RequestResult<Unit> {
        return exMusicRepo.deleteDownloadedSong(songId)
    }
}