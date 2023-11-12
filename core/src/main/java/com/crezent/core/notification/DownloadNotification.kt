package com.crezent.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

import androidx.core.app.NotificationCompat

class DownloadNotification(private val context:Context) {



     val notificationManager by lazy {
        context.getSystemService(NotificationManager::class.java) as NotificationManager
    }


    fun  createChannel(channelId: String,){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(
                channelId,
                "Ex Music",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null,null)
            notificationChannel.setShowBadge(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun buildNotification(
        title:String,
        description:String,
        autoCancel:Boolean = true,
        channelId: String,
        icon:Int,
        percent:Int? = null
        ): Notification {

        createChannel(channelId)
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(context,0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        val notificationBuilder = NotificationCompat.Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(autoCancel)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(icon)
            .apply {
                if (percent != null){
                    setProgress(100, percent, true)

                }
            }
        return  notificationBuilder.build()
    }

}