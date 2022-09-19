package com.athkar.sa.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.getAlarmNotificationPendingIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import kotlin.time.Duration.Companion.milliseconds

@HiltWorker
class ScheduleAlarmPrayNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: Repository,
    private val alarmManager: AlarmManager,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val timeNow = HijrahDate.now()
        val getPrayInfoToday = repository.getPrayInfoByDayAndMonth(
            timeNow.get(ChronoField.DAY_OF_MONTH),
            timeNow.get(ChronoField.MONTH_OF_YEAR)
        )
        val getPendingIntentIfAvailable =
            getAlarmNotificationPendingIntent(applicationContext, PendingIntent.FLAG_NO_CREATE)
        if (getPendingIntentIfAvailable != null) {
            alarmManager.cancel(getPendingIntentIfAvailable)
            getPendingIntentIfAvailable.cancel()
        }
        if (getPrayInfoToday != null) {
            val getNotificationPray = repository.getPrayNotificationById()
            val getNotificationDetails =
                getPrayInfoToday.getNotificationPrayDetails(getNotificationPray!!)
            if (getNotificationDetails != null) {
                val pendingIntentAlarmNotification =
                    getAlarmNotificationPendingIntent(
                        applicationContext,
                        prayName = getNotificationDetails.prayName.namePray
                    )
                pendingIntentAlarmNotification?.let {
                    val triggerTimeNextPray =
                        getNotificationDetails.calculateTriggerTimeMillieByPrayName()

                    // TODO: change alarm type to wall clock
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + triggerTimeNextPray,
                        it
                    )

                }
            } else {
              // add crashlytics
            }
        } else {
            // TODO: later add crashlytics
        }
        return Result.success()
    }
}