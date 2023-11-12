package com.crezent.user_domain.use_case

import com.crezent.data.repository.ExMusicRepo

class RecentPlayedActionUseCase(
    private val exMusicRepo: ExMusicRepo ){
    suspend operator fun invoke(
        shouldDelete:Boolean = false,
        song: com.crezent.models.Song
    ){
       if (shouldDelete){
           exMusicRepo.removeFromRecent(song.songId)
       }
        else{
            val recentlyPlayed = com.crezent.models.RecentlyPlayed(
                songId = song.songId,
                description = song.description,
                thumbnail = song.thumbnailUrl,
                title = song.title
            )
            exMusicRepo.addToRecentlyPlayed(recentlyPlayed)
        }
    }

}