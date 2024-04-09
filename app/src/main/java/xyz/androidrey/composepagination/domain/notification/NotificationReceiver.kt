package com.volarious.vlinestatus.domain.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "ACTION_DISMISS") {
            val notificationId = intent.getIntExtra("notificationId", 0)
            if (notificationId != 0) {
                NotificationManagerCompat.from(context!!).cancel(notificationId)
            }
        }
    }
}