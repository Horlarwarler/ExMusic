package com.example.talimlectures

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LectureApplication : Application(), Configuration.Provider{
    override fun onCreate() {
        super.onCreate()
        // if the build version is greater than or equals to Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // crate a notification channel
            val notificationChannel = NotificationChannel(
                "downloadLecture",
                "lecture Download",
                NotificationManager.IMPORTANCE_HIGH
            )
            //Create a notification manager
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }


}