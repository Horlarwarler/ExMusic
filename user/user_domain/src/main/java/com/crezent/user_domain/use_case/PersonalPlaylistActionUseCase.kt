package com.crezent.user_domain.use_case

import com.crezent.data.repository.ExMusicRepo

class PersonalPlaylistActionUseCase (
    private val exMusicRepo: ExMusicRepo
) {

   suspend operator fun invoke(
       playlist: com.crezent.models.PersonalPlaylist,
       shouldUpdate:Boolean = false
       ){
       if (shouldUpdate){
           exMusicRepo.updatePersonalPlaylist(playlist)
           return
       }
       val alreadyExist =  exMusicRepo.getPersonalPlaylistById(playlist.songId) != null
       if (alreadyExist){
           exMusicRepo.removeFromPersonalPlaylist(playlist.songId)
       }
       else{
           exMusicRepo.addToPersonalPlaylist(playlist)
       }

   }
}