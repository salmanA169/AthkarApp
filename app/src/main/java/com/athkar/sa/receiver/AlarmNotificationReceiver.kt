package com.athkar.sa.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.WorkManager
import com.athkar.sa.Constants
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.rescheduleAlarmNotificationWorker
import com.athkar.sa.uitls.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var  notificationManager :NotificationManager

    @Inject
    lateinit var workManager: WorkManager
    override fun onReceive(context: Context, intent: Intent?) {
        val getPrayName = intent?.getStringExtra(Constants.ALARM_DATA_PRAY_NAME)?:return
        notificationManager.sendNotification(context,getPrayName)
        workManager.rescheduleAlarmNotificationWorker()
    }
}