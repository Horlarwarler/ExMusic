package com.example.talimlectures.presentation.homeScreen

sealed class HomeScreenEvents{
    data class onTextChange(val text:String): HomeScreenEvents()
    object onSearchClick : HomeScreenEvents()
    data class onFavouriteClick(val selectedLecture: String): HomeScreenEvents()
    data class onDownloadClick(val selectedLecture: String): HomeScreenEvents()
    data class onPlayClick(val selectedLecture: String): HomeScreenEvents()
    data class stopDownload (val selectedLecture: String): HomeScreenEvents()
    object onMusicPause:HomeScreenEvents()
    object  onResumeClicked:HomeScreenEvents()
    object onStop:HomeScreenEvents()
    class onNavigate(val screenName:String):HomeScreenEvents()

}
