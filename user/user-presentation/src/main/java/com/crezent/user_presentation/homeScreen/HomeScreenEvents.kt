package com.crezent.user_presentation.homeScreen

sealed class HomeScreenEvents{
     class onTextChange(val text:String): HomeScreenEvents()
    object onSearchClick : HomeScreenEvents()
    class onFavouriteClick(val selectedLectureUniqueId: String): HomeScreenEvents()
     class onDownloadClick(val selectedLectureUniqueId: String): HomeScreenEvents()
     class onPlayClick(val selectedLectureUniqueId: String): HomeScreenEvents()
     class stopDownload (val selectedLectureUniqueId: String): HomeScreenEvents()
    object onMusicPause: HomeScreenEvents()
    object  onResumeClicked: HomeScreenEvents()
    object onStop: HomeScreenEvents()
    class onNavigate(val screenName:String): HomeScreenEvents()
    class onUsernameChanged(val username: String): HomeScreenEvents()
    class onPasswordChanged(val password:String) : HomeScreenEvents()
    object onLoginIconClick : HomeScreenEvents()
    object onLogin: HomeScreenEvents()
    object  cancleLogin: HomeScreenEvents()

    object RemoveShownMessage:HomeScreenEvents()

}
