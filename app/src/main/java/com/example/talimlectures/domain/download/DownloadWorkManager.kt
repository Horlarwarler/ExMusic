package com.example.talimlectures.domain.download

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.talimlectures.R
import com.example.talimlectures.data.local.repository.LectureRepoInterface
import com.example.talimlectures.domain.network.LecturesInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.io.FileOutputStream
import java.util.*

import kotlin.random.Random


@HiltWorker
class DownloadWorkManager @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val api: LecturesInterface,
    private val lectureRepoInterface: LectureRepoInterface
) : CoroutineWorker(context, workerParameters) {

    private val selectedMusic = inputData.getString("selectedMusic")!!

    override suspend fun doWork(): Result {
        val workRequest = lectureRepoInterface.getRequest(selectedMusic)
        val response = api.getLecture(selectedMusic)
        response.body()?.let {
            startForegroundService()
            val file = File(context.filesDir, selectedMusic)
            val outputStream = FileOutputStream(file)

            try {
                outputStream.use { stream ->
                    response.body()?.apply {
                        stream.write(response.body()!!.bytes())
                    }
                }

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }finally {
                lectureRepoInterface.deleteRequest(workRequest)
                outputStream.close()
                outputStream.flush()
            }
        }
        if(!response.isSuccessful){
            if (response.errorBody().toString().startsWith("5")){
                Result.retry()
            }
            else{
                lectureRepoInterface.deleteRequest(workRequest)
            }
            Result.failure()
        }
        return Result.failure()
    }



    //setting up foregrand Service
    private  suspend fun  startForegroundService(){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context,"downloadLecture")
                    .setSmallIcon(R.drawable.download)
                    .setContentTitle("Downloading")
                    .setContentText("downloading $selectedMusic}")
                    .build()
            )
        )
    }

    private  fun deleteWork(worKId:UUID,context: Context){
        WorkManager.getInstance(context).cancelWorkById(worKId)
    }
}