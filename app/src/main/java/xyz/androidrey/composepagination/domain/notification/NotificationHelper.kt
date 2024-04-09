package com.volarious.vlinestatus.domain.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import xyz.androidrey.composepagination.domain.service.TheForegroundService
import javax.inject.Inject

class NotificationHelper @Inject constructor(private val context: Context) {

    companion object {
        const val SERVICE_NOTIFICATION_ID = 1
        const val SERVICE_CHANNEL = "Background"
    }

    fun createNotificationChannel(
        channelId: String,
        importance: Int = NotificationManager.IMPORTANCE_LOW
    ) {
        val channel = NotificationChannel(channelId, channelId, importance).apply {
            setSound(null, null)
        }
        context.getSystemService(NotificationManager::class.java)
            ?.createNotificationChannel(channel)
    }

    fun createNotification(
        channelId: String,
        title: String,
        content: String,
        icon: Int,
        intentClass: Class<*>,
        flags: Int = PendingIntent.FLAG_IMMUTABLE
    ): Notification {
        var pendingIntent: PendingIntent? = null
        val notificationIntent = Intent(context, intentClass)
        if (intentClass == TheForegroundService::class.java) {
            notificationIntent.action = "STOP_SERVICE"
            pendingIntent = PendingIntent.getService(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                context,
                0, notificationIntent, flags
            )
        }

        var notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setContentTitle(title)
            setContentText(content)
            setSmallIcon(icon)
            setPriority(NotificationCompat.PRIORITY_MAX)
            setTimeoutAfter(5000)
        }
        if (intentClass == TheForegroundService::class.java) {
            notificationBuilder.addAction(android.R.drawable.ic_delete, "Stop", pendingIntent)
        } else {
            notificationBuilder.setContentIntent(pendingIntent)
        }
        return notificationBuilder.build()
    }
}