package com.example.talimlectures.domain.download

import kotlinx.coroutines.Job

data class DownloadingLecture(
    val lectureName: String? = null,
    val progress:Double = 0.0,
    val job:Job? = null

)
