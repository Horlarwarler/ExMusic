package com.crezent.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.crezent.core.R

class AudioNotification(
    private val context:Context
) : Notification {

    override val notificationManager: NotificationManager = context.getSystemService(NotificationManager::class.java) as NotificationManager


    init {

    }
    override fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
            val channel = NotificationChannel(AUDIO_CHANNEL,"Audio Notification", NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun buildNotification(
      //  playbackState: com.crezent.music.PlaybackState?
    ) {
//        val notificationBuilder = NotificationCompat.Builder(context, AUDIO_CHANNEL)
//            .setSmallIcon(R.drawable.logo)
//            .setContentTitle("Playing ${playbackState!!.mediaItem?.title}")
//            .setContentText(playbackState.mediaItem?.description)
//            .setAutoCancel(false)
//            .setProgress(playbackState.contentDuration.toInt(), playbackState.currentPosition.toInt(), false)
//        TODO("Not yet implemented")
    }

    companion object {
        const val AUDIO_CHANNEL = "AUDIO_CHANNEL"
    }
}