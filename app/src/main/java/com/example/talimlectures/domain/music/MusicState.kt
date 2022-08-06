package com.example.talimlectures.domain.music

import com.example.talimlectures.data.model.DatabaseLectureModel

data class MusicState (
    val currentLecture: DatabaseLectureModel? =null,
    val selectedLecture: DatabaseLectureModel? = null,
    val isPaused: Boolean = false,
    val playerVisible: Boolean = false,
    val totalTime:Double = 0.0,
    val totalTimeDisplay:String = ": :",
    val currentTimeDisplay:String = ": :",
    val currentTime:Double = 0.1,
    val miniPlayerVisible:Boolean= false,
    val lectures:List<DatabaseLectureModel> = emptyList(),
    val lecture: DatabaseLectureModel? = null,
    val downloadLectures:List<String> = emptyList()
)