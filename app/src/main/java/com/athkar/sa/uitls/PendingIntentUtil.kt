package com.athkar.sa.uitls

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.athkar.sa.Constants
import com.athkar.sa.receiver.AlarmNotificationReceiver

const val REQUEST_CODE_ALARM_RECEIVER = 1
fun getAlarmNotificationPendingIntent(
    context: Context,
    flag: Int = PendingIntent.FLAG_UPDATE_CURRENT,
    prayName: String? = null
): PendingIntent? =
    PendingIntent.getBroadcast(
        context,
        REQUEST_CODE_ALARM_RECEIVER,
        Intent(context, AlarmNotificationReceiver::class.java).apply {
            if (prayName != null) {
                putExtra(Constants.ALARM_DATA_PRAY_NAME, prayName)
            }
        },
        flag or PendingIntent.FLAG_IMMUTABLE
    )
