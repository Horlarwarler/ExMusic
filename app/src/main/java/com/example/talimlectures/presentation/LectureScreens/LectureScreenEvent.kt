package com.example.talimlectures.presentation.LectureScreens

import com.example.talimlectures.presentation.homeScreen.HomeScreenEvents

sealed class LectureScreenEvent{
    data class onTextChange(val text:String): LectureScreenEvent()
    object onSearchClick : LectureScreenEvent()
    data class onDownloadClick(val selectedLecture: String): LectureScreenEvent()
    data class onPlayClick(val selectedLecture: String): LectureScreenEvent()
    data class stopDownload (val selectedLecture: String): LectureScreenEvent()
    object onMusicPause: LectureScreenEvent()
    object  onResumeClicked: LectureScreenEvent()
    object onStop: LectureScreenEvent()
    class onNavigate(val screenName:String):LectureScreenEvent()

}
