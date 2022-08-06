package com.example.talimlectures.presentation.PlayerScreens

sealed class PlayingScreenEvent(){
    class OnFavouriteClick(val isFavourite:Boolean): PlayingScreenEvent()
    object Pause: PlayingScreenEvent()
    object Resume:PlayingScreenEvent()
    object FastForward:PlayingScreenEvent()
    object Rewind:PlayingScreenEvent()
    object  Next:PlayingScreenEvent()
    object Previous:PlayingScreenEvent()
    object OnDownloadClick:PlayingScreenEvent()
    object StopDownload:PlayingScreenEvent()

}
