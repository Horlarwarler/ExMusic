package com.example.talimlectures.domain.download

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.*
import androidx.work.WorkManager
import com.example.talimlectures.data.local.repository.LectureRepoInterface
import com.example.talimlectures.data.model.WorkRequest
import com.example.talimlectures.domain.network.LecturesInterface
import kotlinx.coroutines.*
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext", "EXPERIMENTAL_IS_NOT_ENABLED")
class DownloadInterfaceImpl @Inject constructor(
    val context: Context,
    private val lectureRepoInterface: LectureRepoInterface
) : DownloadInterface{


    override var downloadState by mutableStateOf(DownloadInterfaceState())

    override fun selectLecture(lectureName: String) {
        downloadState = downloadState.copy(selectedMusic= lectureName)
    }
    @OptIn(InternalCoroutinesApi::class)
    override suspend fun download(){
        val selectedMusic = downloadState.selectedMusic
        val workData = workDataOf("selectedMusic" to selectedMusic)
        val constraints = Constraints.Builder().setRequiredNetworkType(
            NetworkType.CONNECTED
        ).build()

        val workRequest = OneTimeWorkRequestBuilder<DownloadWorkManager>()
            .setConstraints(constraints)
            .setInputData(workData)
            .build()
        val newRequest = WorkRequest(
            lectureName = selectedMusic!!,
            workId = workRequest.id
        )
        lectureRepoInterface.insertRequest(newRequest)
        WorkManager.getInstance(context).enqueue(workRequest)
    }


    override suspend fun stopDownload(

    ) {
        val selectedMusic = downloadState.selectedMusic!!
        val request = lectureRepoInterface.getRequest(selectedMusic)
        lectureRepoInterface.deleteRequest(request)
        WorkManager.getInstance(context).cancelWorkById(request.workId)

    }

}