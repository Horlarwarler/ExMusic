package com.crezent.user_presentation.music


sealed class MusicEvent {
    object RemoveShownMessage:MusicEvent()
    class PlaylistAction(val song: com.crezent.models.Song):MusicEvent()

    class AddToFavourite(val songId:String): MusicEvent()
    class RemoveFromFavourite(val songId: String): MusicEvent()

    class SelectSong(val  songId: String): MusicEvent()

}