package com.example.talimlectures.domain.music

import com.example.talimlectures.data.model.DatabaseLectureModel

interface MusicInterface {
    suspend fun  initialised(lectures:List<DatabaseLectureModel>)
    suspend fun playMusic(playFromMusic:Boolean = false)
    fun stopMusic()
    suspend fun finishedMusic()
    suspend fun fastForward()
    suspend fun skipBackward()
    suspend fun nextMusic()
    suspend fun previousMusic()
    fun pauseMusic()
    suspend fun restartMusic()
    suspend fun resumeMusic()
    fun selectMusic(musicName:DatabaseLectureModel)

    val musicState:MusicState
}