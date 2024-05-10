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
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.athkar.sa.audio.PlayerAudioManager
import com.athkar.sa.audio.PlayerMediaEvent
import com.athkar.sa.repo.Repository
import com.athkar.sa.uitls.enqueueScheduleAlarmNotificationWorker
import com.athkar.sa.uitls.rescheduleAlarmNotificationWorker
import com.athkar.sa.worker.ScheduleAlarmPrayNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val workManager: WorkManager,
    private val alarmManager: AlarmManager,
    private val audioManager: PlayerAudioManager
) : ViewModel() {

    val getPrayInfo = repository.getPrayInfo()

    private val _effect = MutableStateFlow<MainEffect?>(null)
    val effect = _effect.asStateFlow()

    suspend fun getPrayInfoToday() = withContext(Dispatchers.IO) {
        val dateHijre = HijrahDate.now()
        repository.getPrayInfoByDayAndMonth(
            dateHijre.get(ChronoField.DAY_OF_MONTH),
            dateHijre.get(ChronoField.MONTH_OF_YEAR)
        )
    }

    fun rescheduleAlarm() {
        workManager.rescheduleAlarmNotificationWorker()
    }

    fun resetEffect() {
        _effect.value = null
    }

    fun setAudioListener(playerListener: PlayerMediaEvent) {
        audioManager.setListener(playerListener)
    }

    fun playAudio(id: String, uri: String,mediaMediaMetadata: MediaMetadata) {
        audioManager.playAudioItem(id, uri,mediaMediaMetadata)
    }

    fun pauseAudio(){
       audioManager.pauseAudio()
    }
    fun resumeAudio(){
        audioManager.resumeAudio()
    }
    fun scheduleAlarm() {
        workManager.apply {
            if (Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms()) {
                    enqueueScheduleAlarmNotificationWorker()
                } else {
                    _effect.update {
                        MainEffect.ShowAlarmDialog
                    }
                }
            } else {
                enqueueScheduleAlarmNotificationWorker()
            }

        }
    }

    fun seekTo(progress: Long) {
        audioManager.seekTo(progress)
    }

    fun forward5() {
        audioManager.forward5()
    }
    fun back5(){
        audioManager.back5()
    }

    fun releaseAudio(){
        audioManager.releaseAudio()
    }
    fun updateStateAudio():Boolean {
        return audioManager.updateStateAudio()
    }
    fun getCurrentMediaInfo ()= audioManager.getCurrentMediaMetaData()
}

sealed class MainEffect {
    data object ShowAlarmDialog : MainEffect()

}