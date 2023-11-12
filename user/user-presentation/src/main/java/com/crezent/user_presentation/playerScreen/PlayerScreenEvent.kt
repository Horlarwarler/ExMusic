package com.crezent.user_presentation.playerScreen

sealed class PlayerScreenEvent(){

    object StartDownload : PlayerScreenEvent()
    object StopDownload : PlayerScreenEvent()
    object PauseDownload : PlayerScreenEvent()
    object RestartDownload : PlayerScreenEvent()
    object ResumeDownload : PlayerScreenEvent()
    object RemoveFromDownload : PlayerScreenEvent()



}
