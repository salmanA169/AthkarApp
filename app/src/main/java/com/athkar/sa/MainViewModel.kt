package com.athkar.sa

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.enqueueScheduleAlarmNotificationWorker
import com.athkar.sa.uitls.rescheduleAlarmNotificationWorker
import com.athkar.sa.worker.ScheduleAlarmPrayNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager,
    private val alarmManager: AlarmManager
) : ViewModel() {

    val getPrayInfo = repository.getPrayInfo()


    suspend fun getPrayInfoToday() = withContext(Dispatchers.IO){
        val dateHijre = HijrahDate.now()
        repository.getPrayInfoByDayAndMonth(
            dateHijre.get(ChronoField.DAY_OF_MONTH),
            dateHijre.get(ChronoField.MONTH_OF_YEAR)
        )
    }
    fun rescheduleAlarm() {
        workManager.rescheduleAlarmNotificationWorker()
    }

    fun scheduleAlarm() {
        workManager.apply {
            if (Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms()) {
                    enqueueScheduleAlarmNotificationWorker()
                } else {
                    context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            } else {
                enqueueScheduleAlarmNotificationWorker()
            }

        }
    }

}