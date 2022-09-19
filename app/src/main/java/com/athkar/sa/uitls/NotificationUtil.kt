package com.athkar.sa.uitls

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.athkar.sa.Constants
import com.athkar.sa.MainActivity
import com.athkar.sa.R

fun NotificationManager.sendNotification(context: Context, message: String) {
    val pendingIntentMainActivity = PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon)
        .setContentText("وقت صلاة $message")
        .setContentIntent(pendingIntentMainActivity)
        .setAutoCancel(true)
        .setColor(context.getColor(R.color.md_theme_light_primary))
        .build()

    notify(1, notification)
}

fun NotificationManager.cancelAllNotification() {
    cancelAll()
}