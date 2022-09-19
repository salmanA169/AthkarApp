package com.athkar.sa.uitls

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.athkar.sa.ConstantsWorker
import com.athkar.sa.worker.ScheduleAlarmPrayNotificationWorker
import java.util.concurrent.TimeUnit

fun WorkManager.enqueueScheduleAlarmNotificationWorker() {
    enqueueUniquePeriodicWork(
        ConstantsWorker.ALARM_WORKER,
        ExistingPeriodicWorkPolicy.KEEP,
        getPeriodicWorker6Hours()
    )
}

fun WorkManager.rescheduleAlarmNotificationWorker(){
    cancelUniqueWork(ConstantsWorker.ALARM_WORKER)
    enqueueScheduleAlarmNotificationWorker()
}

fun getPeriodicWorker6Hours() = PeriodicWorkRequestBuilder<ScheduleAlarmPrayNotificationWorker>(
    6, TimeUnit.HOURS
).build()