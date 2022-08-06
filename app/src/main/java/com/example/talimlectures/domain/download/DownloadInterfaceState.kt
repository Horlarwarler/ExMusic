package com.example.talimlectures.domain.download

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class DownloadInterfaceState(
    val selectedMusic: String? = null,
    val downloadJobs: HashMap<String, DownloadingLecture> = HashMap(),
    val downloadState : MutableSharedFlow<DownloadOptions> = MutableSharedFlow<DownloadOptions>()
)
