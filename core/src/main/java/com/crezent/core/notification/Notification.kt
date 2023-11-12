package com.crezent.core.notification

import android.app.NotificationManager

interface Notification {

    val notificationManager: NotificationManager
    fun createChannel()

    fun buildNotification(
//        playbackState: com.crezent.music.PlaybackState? = null
    )
}